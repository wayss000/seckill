package org.seckill.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Wayss
 * @version 2017-3-27 下午11:59:32
 */

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    
    //注入Dao实现类依赖
    @Resource
    private SuccessKilledDao successKillDao;
    
    @Test
    public void testInsertSuccessKilled() throws Exception{
//        long id = 1000;
        long id = 1001;
        long phone = 12345678909L;
        int insertCount = successKillDao.insertSuccessKilled(id,phone);
        System.out.println("insertCount:" + insertCount);
        //第一次结果：insertCount:1
        //第二次结果：insertCount:0
    }
    
    @Test
    public void testQueryByIdWithSeckill() throws Exception{
//        long id = 1000;
        long id = 1001;
        long phone = 12345678909L;
        SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(id);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckillId());
        //结果1
        //SuccessKilled [seckillId=1000, userPhone=12345678909, state=-1, createTime=Tue Mar 28 00:16:51 CST 2017]
        //1000
        //结果2
        //SuccessKilled [seckillId=1001, userPhone=12345678909, state=0, createTime=Tue Mar 28 00:36:03 CST 2017]
        //1001
    }
    
}
