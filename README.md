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

## 项目结构说明
- `entity`: 实体类，如User
- `mapper`: MyBatis-Plus的Mapper接口
- `service`: 服务层接口及实现
- `util`: 工具类，如JwtUtil
- `config`: 配置类，如RedisConfig、SecurityConfig
- `controller`: 控制器层，处理HTTP请求
- `security`: 安全相关类，如JWT过滤器和用户详情服务

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

### 邮件相关
- `POST /api/email/simple`: 发送简单文本邮件
- `POST /api/email/html`: 发送HTML邮件
- `POST /api/email/attachment`: 发送带附件的邮件

### 文件存储相关
- `POST /api/files/upload`: 上传文件
- `GET /api/files/download/{filename}`: 下载文件
- `DELETE /api/files/delete/{filename}`: 删除文件
- `GET /api/files/list`: 获取文件列表

## 安全配置
- 使用Spring Security进行安全控制
- 基于JWT的无状态认证
- 密码使用BCrypt加密
- 配置了公开访问的API路径
- 集成Swagger UI，方便API文档查看和测试

## 数据库
数据库初始化脚本位于 `src/main/resources/db/` 目录：
- `schema.sql`: 数据库表结构
- `data.sql`: 测试数据

## 启动项目
1. 确保已安装MySQL和Redis
2. 确保已安装MinIO（或使用远程MinIO服务）
3. 执行数据库初始化脚本
4. 修改 `application.yml` 中的数据库、Redis和MinIO配置
5. 运行 `mvn spring-boot:run` 启动应用
6. 访问 `http://localhost:8080/swagger-ui/index.html` 查看API文档