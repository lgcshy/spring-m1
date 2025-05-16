# Java 新手的 Spring boot 之旅

## 我的电脑环境
- 已安装sdkman管理jdk版本

## spring boot 版本选择
- Project maven
- Spring Boot 3.3.11
- java version 21

## 需要完成的事项
- ✅ mybatis-plus的使用
- ✅ redis的使用
- ✅ jwt
- ✅ 序列化

## 项目结构说明
- `entity`: 实体类，如User
- `mapper`: MyBatis-Plus的Mapper接口
- `service`: 服务层接口及实现
- `util`: 工具类，如JwtUtil
- `config`: 配置类，如RedisConfig
- `controller`: 控制器层，处理HTTP请求

## 功能实现
- MyBatis-Plus: 实现了基本的CRUD操作
- Redis: 使用RedisTemplate进行缓存操作，配置了Jackson序列化
- JWT: 实现了JWT令牌的生成和验证
- 序列化: 在RedisConfig中配置了Jackson序列化器

## API接口
### 用户相关
- `POST /api/users/register`: 用户注册
- `POST /api/users/login`: 用户登录
- `GET /api/users/info`: 获取用户信息（需要token）
- `POST /api/users/logout`: 退出登录（需要token）

### 缓存相关
- `POST /api/cache/set`: 设置缓存
- `GET /api/cache/get`: 获取缓存
- `DELETE /api/cache/delete`: 删除缓存

## 安全机制
- 使用JWT实现无状态的身份验证
- 使用Redis存储token，支持token的刷新和注销
- 实现了JWT认证过滤器，自动拦截和验证需要认证的请求
- 统一的异常处理和响应格式

## 数据库
数据库初始化脚本位于 `src/main/resources/db/` 目录：
- `schema.sql`: 数据库表结构
- `data.sql`: 测试数据

## 启动项目
1. 确保已安装MySQL和Redis
2. 执行数据库初始化脚本
3. 修改 `application.yml` 中的数据库和Redis配置
4. 运行 `SpringM1Application.java` 启动项目