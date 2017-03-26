package org.seckill.dao;

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
        long id=1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }
    
    @Test
    public void testQueryById() throws Exception{
        
    }
    
    @Test
    public void testQueryAll() throws Exception{
        
    }
    
}
