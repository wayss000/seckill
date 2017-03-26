package org.seckill.dao;

import org.seckill.entity.SuccessKilled;

/**
 * @author Wayss
 * @version 2017-3-26 下午11:08:06
 */

public interface SuccessKilledDao {

    /**
     * 插入购买明细s,可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入的行数量
     */
    int insertSuccessKilled(long seckillId, long userPhone);
    
    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(long seckillId);
    
}
