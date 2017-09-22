CREATE DATABASE seckill;

use seckill;

CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT  '库存数量',
`start_time` timestamp NOT NULL COMMENT '秒杀开启时间',
`end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 CHARSET=utf8 COMMENT='秒杀库存表';


-- 初始化数据
insert into 
    seckill(name,number,start_time,end_time)
value
    ('1000RMB SecKill iphone6s',100,'2017-11-11 00:00:00','2017-11-12 00:00:00'),
    ('500RMB SecKill ipad2',100,'2017-11-01 00:00:00','2017-11-12 00:00:00'),
    ('300RMB SecKill XiaoMi4',100,'2017-11-11 00:00:00','2017-11-12 00:00:00'),
    ('200RMB SecKill RedMi note',100,'2017-11-11 00:00:00','2017-11-12 00:00:00');
    
-- 秒杀成功明细表
-- 用户登录认证相关信息
CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
`user_phone` bigint NOT NULL COMMENT '用户手机号',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态表示：-1：无效   0：成功   1：已付款',
`create_time` timestamp NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckill_id,user_phone),/*联合主键*/
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 CHARSET=utf8 COMMENT='秒杀成功明细表';


-- 连接数据库控制台
mysql -uroot -p123456
