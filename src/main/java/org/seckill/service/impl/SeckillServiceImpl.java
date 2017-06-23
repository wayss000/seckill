package org.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * 
 * @author Wayss
 */
//@Component 代表所有的组件(包含下面的具体的)
//@Service  @Dao  @Controller
@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    //注入Service依赖
    @Autowired//自动注入    //@Resource  @Inject
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;
    
    @Autowired
    private RedisDao redisDao;
    
    //md5盐值,用于混淆md5
    private final String slat = ";asdkjffhsda&(*()";
    
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化
        /**
         * get from cache
         * if null
         *   get db
         *   else
         *     put cache
         *  logical
         */
        //1.访问Redis
        Seckill seckill = redisDao.getSecill(seckillId);
        if(seckill == null){
            //访问数据库
            seckill = seckillDao.queryById(seckillId);
            if(seckill == null){
                return new Exposer(false, seckillId);
            }else{
                redisDao.putSeckill(seckill);
            }
            
        }
        
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getCreateTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程,不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }
    
    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    /**
     * 使用注解控制事务方法的优点
     * 1:开发团队达成一致,明确注明编写事务的方法
     * 2:保证事务方法的执行事件尽可能短,不要穿插其他网络操作RPC/HTTP请求或者剥离事务方法外部
     * 3:不是所有的方法都需要事务,如,只有一条修改操作或者只读操作不需要事务控制,
     */
/*    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑:减库存 + 记录购买行为
        Date nowTime = new Date();
        try {
            // 减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                // 没有更新到记录,秒杀结束
                throw new SeckillCloseException("seckill is close");
            }else{
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    // 重复秒杀
                    throw new RepeatKillException("seckill repeat");
                } else {
                    // 秒杀成功
                    List<SuccessKilled> successKilledLst = successKilledDao.queryByIdWithSeckill(seckillId);
                    SuccessKilled successKilled = null;
                    for(SuccessKilled temp : successKilledLst){
                        if(temp.getUserPhone() == userPhone){
                            successKilled = temp;
                            break;
                        }
                    }
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常,转化为运行期异常
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }*/
    
    //优化，重写此方法
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑: 记录购买行为 + 减库存(相比上个方法,可以减少一次无效数据库查询) 
        // 原因:insert不会有行级锁,update会产生行级锁,
        Date nowTime = new Date();
        try {
          //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeat");
            } else {
                // 减库存,热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // 没有更新到记录,秒杀结束,rollback
                    throw new SeckillCloseException("seckill is close");
                }else{
                    // 秒杀成功,  commit
                    List<SuccessKilled> successKilledLst = successKilledDao.queryByIdWithSeckill(seckillId);
                    SuccessKilled successKilled = null;
                    for(SuccessKilled temp : successKilledLst){
                        if(temp.getUserPhone() == userPhone){
                            successKilled = temp;
                            break;
                        }
                    }
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
            
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常,转化为运行期异常
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }

    public SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5){
        
        return null;
    }

}
