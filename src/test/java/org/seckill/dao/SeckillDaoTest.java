package org.seckill.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * @author Wayss
 * @version 2017-3-27 上午12:39:46
 */

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    
    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;
    
    @Test
    public void testReduceNumber() throws Exception{
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L,killTime);
        System.out.println("updateCount=" + updateCount);
        //结果：updateCount=0
    }
    
    @Test
    public void testQueryById() throws Exception{
        long id=1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        //结果
        //1000元秒杀iphone6s
        //Seckill [seckillId=1000, name=1000元秒杀iphone6s, number=100, startTime=Tue Nov 01 00:00:00 CST 2016, endTime=Wed Nov 02 00:00:00 CST 2016, createTime=Sun Mar 26 22:40:20 CST 2017]

    }
    
    @Test
    public void testQueryAll() throws Exception{
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for(Seckill seckill : seckills){
            System.out.println(seckill);
        }
        //结果
        //Seckill [seckillId=1000, name=1000元秒杀iphone6s, number=100, startTime=Tue Nov 01 00:00:00 CST 2016, endTime=Wed Nov 02 00:00:00 CST 2016, createTime=Sun Mar 26 22:40:20 CST 2017]
        //Seckill [seckillId=1001, name=500元秒杀ipad2, number=100, startTime=Tue Nov 01 00:00:00 CST 2016, endTime=Wed Nov 02 00:00:00 CST 2016, createTime=Sun Mar 26 22:40:20 CST 2017]
        //Seckill [seckillId=1002, name=300元秒杀小米4, number=100, startTime=Tue Nov 01 00:00:00 CST 2016, endTime=Wed Nov 02 00:00:00 CST 2016, createTime=Sun Mar 26 22:40:20 CST 2017]
        //Seckill [seckillId=1003, name=200元秒杀红米note, number=100, startTime=Tue Nov 01 00:00:00 CST 2016, endTime=Wed Nov 02 00:00:00 CST 2016, createTime=Sun Mar 26 22:40:20 CST 2017]
    }
    
}
