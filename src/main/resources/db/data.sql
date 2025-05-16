-- 使用数据库
USE spring_m1;

-- 插入测试用户数据
INSERT INTO `user` (`username`, `password`, `email`, `create_time`, `update_time`, `deleted`)
VALUES
('admin', 'admin123', 'admin@example.com', NOW(), NOW(), 0),
('test', 'test123', 'test@example.com', NOW(), NOW(), 0); 