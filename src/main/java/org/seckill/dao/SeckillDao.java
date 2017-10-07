package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

/**
 * @author Wayss
 * @version 2017-3-26 下午11:03:33
 */

public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 如果影响行数>1,表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId")long seckillId, @Param("killTime")Date killTime);
    
    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);
    
    /**
     * 根据偏移量查询秒杀商品列表
     * @param offet
     * @param limit
     * @return
     */
    //这个接口有两个参数，mybatis需要注解才能识别出
    List<Seckill> queryAll(@Param("offset")int offset, @Param("limit")int limit);
    
    /**
     * 通过存储过程执行秒杀操作
     * @param paramMap
     */
    void killByProcedure(Map<String,Object> paramMap);
}
