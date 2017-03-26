package org.seckill.entity;

import java.util.Date;

/**
 * @author Wayss
 * @version 2017-3-26 下午10:58:18
 */

public class SuccessKilled {
    
    private long seckillId;
    
    private long userPhone;
    
    private short state;
    
    private Date createTime;

    //多对一,复合属性，一个实体有可能对应多个记录
    private Seckill seckill;
    
    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled [seckillId=" + seckillId + ", userPhone=" + userPhone + ", state=" + state
                + ", createTime=" + createTime + "]";
    }

}
