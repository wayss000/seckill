package org.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Wayss
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:spring/spring-dao.xml",
    "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void testGetById() {
        Seckill seckill = seckillService.getById(1000);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testExportSeckillUrl() {
        long id = 1000;
        Exposer export = seckillService.exportSeckillUrl(id);
        logger.info("export:" + export);
        //Exposer [exposed=true, md5=3c862cc4786b19893f1c9d8ee13b9566, seckillId=1000, now=0, start=0, end=0]
    }

    @Test
    public void testExecuteSeckill() {
        long id = 1000;
        long phoneNumber = 12345678701L;
        String md5 = "3c862cc4786b19893f1c9d8ee13b9566";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(id, phoneNumber, md5);
            logger.info("seckillExecution" + seckillExecution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }
        /**
         *
22:42:31.269 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Creating a new SqlSession
22:42:31.276 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@f14a7d4]
22:42:31.282 [main] DEBUG o.m.s.t.SpringManagedTransaction - JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@1bab8268] will be managed by Spring
22:42:31.288 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==>  Preparing: update seckill set number = number - 1 where seckill_id = ? and start_time <= ? and end_time >= ? and number > 0; 
22:42:31.325 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==> Parameters: 1000(Long), 2017-06-11 22:42:31.256(Timestamp), 2017-06-11 22:42:31.256(Timestamp)
22:42:31.330 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - <==    Updates: 1
22:42:31.331 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@f14a7d4]
22:42:31.331 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@f14a7d4] from current transaction
22:42:31.331 [main] DEBUG o.s.d.S.insertSuccessKilled - ==>  Preparing: insert ignore into success_killed(seckill_id,user_phone,state) values (?,?,0) 
22:42:31.333 [main] DEBUG o.s.d.S.insertSuccessKilled - ==> Parameters: 1000(Long), 12345678709(Long)
22:42:31.334 [main] DEBUG o.s.d.S.insertSuccessKilled - <==    Updates: 1
22:42:31.344 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@f14a7d4]
22:42:31.344 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@f14a7d4] from current transaction
22:42:31.345 [main] DEBUG o.s.d.S.queryByIdWithSeckill - ==>  Preparing: select sk.seckill_id, sk.user_phone, sk.create_time, sk.state, s.seckill_id "seckill.seckill_id", s.name "seckill.name", s.number "seckill.number", s.start_time "seckill.start_time", s.end_time "seckill.end_time", s.create_time "seckill.create_time" from success_killed sk inner join seckill s on sk.seckill_id = s.seckill_id where sk.seckill_id=? 
22:42:31.346 [main] DEBUG o.s.d.S.queryByIdWithSeckill - ==> Parameters: 1000(Long)
22:42:31.374 [main] DEBUG o.s.d.S.queryByIdWithSeckill - <==      Total: 1
22:42:31.381 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@f14a7d4]
22:42:31.382 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization committing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@f14a7d4]
22:42:31.382 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization deregistering SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@f14a7d4]
22:42:31.382 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization closing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@f14a7d4]
22:42:31.419 [main] INFO  o.seckill.service.SeckillServiceTest - seckillExecutionSeckillExecution [seckillId=1000, state=1, stateInfo=秒杀成功, successKilled=SuccessKilled [seckillId=1000, userPhone=12345678709, state=0, createTime=Sun Jun 11 22:42:31 CST 2017]]
         */
    }
    
    //整合testExportSeckillUrl()和testExecuteSeckill()
    //注意测试代码完整逻辑,注意可重复性
    @Test
    public void testSeckillLogic(){
        long id = 1000;
        Exposer export = seckillService.exportSeckillUrl(id);
        if(export.isExposed()){
            logger.info("export:" + export);
            long phoneNumber = 12345678702L;
            String md5 = export.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phoneNumber, md5);
                logger.info("seckillExecution" + seckillExecution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        }else{
            //秒杀未开启
            logger.warn("export:" + export);
        }
    }

}
