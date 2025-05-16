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
- ✅ 邮件发送
- ✅ MinIO文件存储
- ✅ Spring Security
- ✅ 数据库迁移(Flyway)
- ✅ 好友关系管理
- ✅ 用户UUID唯一标识

## 项目结构说明
- `entity`: 实体类，如User、Friend
- `mapper`: MyBatis-Plus的Mapper接口
- `service`: 服务层接口及实现
- `util`: 工具类，如JwtUtil
- `config`: 配置类，如RedisConfig、SecurityConfig
- `controller`: 控制器层，处理HTTP请求
- `security`: 安全相关类，如JWT过滤器和用户详情服务
- `db/migration`: Flyway数据库迁移脚本

## 功能实现
- **MyBatis-Plus**: 实现了基本的CRUD操作
- **Redis**: 使用RedisTemplate进行缓存操作，配置了Jackson序列化
- **JWT**: 实现了JWT令牌的生成和验证
- **序列化**: 在RedisConfig中配置了Jackson序列化器
- **邮件**: 实现了简单文本邮件、HTML邮件和带附件的邮件发送
- **文件存储**: 使用MinIO实现文件上传、下载、删除和列表功能
- **Spring Security**: 实现了基于JWT的认证和授权
  - 使用SecurityFilterChain配置安全规则
  - 实现了JwtAuthenticationFilter过滤器处理JWT令牌
  - 自定义UserDetailsService加载用户信息
  - 配置了BCryptPasswordEncoder进行密码加密
  - 配置了不需要认证的路径，如登录、注册、Swagger UI等
- **数据库迁移**: 使用Flyway管理数据库版本和迁移
  - 自动执行SQL脚本创建或更新数据库结构
  - 支持版本控制和回滚
- **好友关系管理**: 实现了好友关系的增删改查
  - 支持发送好友请求
  - 支持接受或拒绝好友请求
  - 支持查看好友列表和好友请求列表
- **用户UUID唯一标识**: 为每个用户添加UUID字段
  - 注册时自动生成UUID
  - 提供通过UUID查询用户的API

## API接口
### 用户相关
- `POST /api/users/register`: 用户注册
- `POST /api/users/login`: 用户登录
- `GET /api/users/info`: 获取用户信息（需要token）
- `GET /api/users/uuid/{uuid}`: 通过UUID获取用户信息（公开访问）
- `POST /api/users/logout`: 退出登录（需要token）

### 缓存相关
- `POST /api/cache/set`: 设置缓存
- `GET /api/cache/get`: 获取缓存
- `DELETE /api/cache/delete`: 删除缓存

### 邮件相关
- `POST /api/email/simple`: 发送简单文本邮件
- `POST /api/email/html`: 发送HTML邮件
- `POST /api/email/attachment`: 发送带附件的邮件

### 文件存储相关
- `POST /api/files/upload`: 上传文件
- `GET /api/files/download/{filename}`: 下载文件
- `DELETE /api/files/delete/{filename}`: 删除文件
- `GET /api/files/list`: 获取文件列表

### 好友关系相关
- `POST /api/friends/request`: 发送好友请求
- `POST /api/friends/accept`: 接受好友请求
- `POST /api/friends/reject`: 拒绝好友请求
- `GET /api/friends/list`: 获取好友列表
- `GET /api/friends/requests`: 获取好友请求列表

## 安全配置
- 使用Spring Security进行安全控制
- 基于JWT的无状态认证
- 密码使用BCrypt加密
- 配置了公开访问的API路径
- 集成Swagger UI，方便API文档查看和测试

## 数据库迁移
数据库迁移脚本位于 `src/main/resources/db/migration/` 目录：
- `V1__Create_friends_table.sql`: 创建好友关系表
- `V2__Add_uuid_to_users_table.sql`: 为用户表添加UUID字段

## 启动项目
1. 确保已安装MySQL和Redis
2. 确保已安装MinIO（或使用远程MinIO服务）
3. 修改 `application.yml` 中的数据库、Redis和MinIO配置
4. 运行 `mvn spring-boot:run` 启动应用
5. 访问 `http://localhost:8080/swagger-ui/index.html` 查看API文档