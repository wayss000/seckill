-- 秒杀执行存储过程
DELIMITER $$ --console ; 转化为$$

-- 定义存储过程
-- 参数: in 输入参数; out 输出参数
-- count_count():返回上一级修改类型sql(delete,insert,update)的影响行数
-- row_count: 0 :未修改数据       >0 表示修改的行数          <0:sql错误
CREATE PROCEDURE `seckill`.`execute_seckill`
    (in v_seckill_id bigint, in v_phone bigint,
    in v_kill_time timestamp, out r_result int)
BEGIN 
    DECLARE insert_count int DEFAULT 0;
    START TRANSACTION;
    insert ignore into success_killed
        (seckill_id, user_phone, create_time)
        value (v_seckill_id, v_phone, v_kill_time)
    select row_count() into insert_count;
    IF (insert_count = 0) THEN
        ROLLBACK;
        set r_result = -1;
    ELSEIF(insert_count < 0) THEN
        ROLLBACK;
        set r_result = -2;
    ELSE
        UPDATE seckill
            set number = number - 1
            where seckill_id = v_seckill_id
		        and end_time > v_kill_time
		        and start_time < v_kill_time
		        and number > 0;
            select row_count() into insert_count;
        IF(insert_count = =0)   THEN
           ROLLBACK;
           set r_result = 0;
        ELSEIF (insert_count < 0) THEN
           ROLLBACK;
           set r_result = -1;
        ELSE
           COMMIT;
           set r_result = 1;
        END IF;
    END IF;
END;
$$ --表示换行
    
-- 存储过程定义结束
DELIMITER ;

set @r_result = -3;
call execute_seckill(1000,13511024618,now(),@r_result);

--获取结果
select @r_result;
