package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @author Wayss
 * @version 2017-3-26 下午11:03:33
 */
public class RedisDao {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private JedisPool jedisPool;
    
    public RedisDao(String ip, int port){
        jedisPool = new JedisPool(ip, port);
    }
    
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
    
    public Seckill getSecill(long seckillId){
        //redis 逻辑操作
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                //并没有实现内部序列化操作
                //get -> byte[] -> 反序列化 ->Object(Seckill)
                //采用自定义序列号
                //protostuff : pojo
                byte[] bytes = jedis.get(key.getBytes());
                //缓存获取到了
                if(bytes != null){
                    //空对象
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill , schema);
                    //seckill被反序列化
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    public String putSeckill(Seckill seckill){
        //set Seckill -> 序列号 -> byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 超时缓存
                int timeout = 60 * 60;//1小时
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                // TODO: handle finally clause
                jedis.close();
            }

        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.getMessage(), e);
        }
        
        return null;
    }
}
