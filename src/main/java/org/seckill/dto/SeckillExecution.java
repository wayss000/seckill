package org.seckill.dto;

/**
 * 封装秒杀后的执行结果
 * @author Wayss
 */

public class SeckillExecution {

    private long seckillId;
    
    //秒杀执行结果状态
    private int state;
    
    //状态表示
    private String stateInfo;
    
    //秒杀成功状态
    private boolean successKilled;

    public SeckillExecution(long seckillId, int state, String stateInfo, boolean successKilled) {
        super();
        this.seckillId = seckillId;
        this.state = state;
        this.stateInfo = stateInfo;
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, int state, String stateInfo) {
        super();
        this.seckillId = seckillId;
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public boolean isSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(boolean successKilled) {
        this.successKilled = successKilled;
    }
}
