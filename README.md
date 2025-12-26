# 微博系统 (Weibo System)

一个基于前后端分离架构的微博系统，支持用户认证、动态发布、社交互动、用户关系管理等核心功能。

## 技术栈

### 后端技术栈
- **核心框架**: Java + Spring Boot 3.2.0
- **认证机制**: JWT (JSON Web Token) 用户认证与授权
- **API设计**: RESTful API，符合RESTful规范
- **数据持久层**: Spring Data JPA + Hibernate ORM
- **数据库**: MySQL 8.0
- **缓存**: Redis 6.2
- **API文档**: Swagger/OpenAPI 3.0

### 前端技术栈
- **核心框架**: Vue 3 + TypeScript
- **状态管理**: Pinia
- **路由管理**: Vue Router
- **网络请求**: Axios
- **UI组件库**: Element Plus

## 功能模块

### 用户管理模块
- 用户注册、登录功能
- 个人资料管理
- 头像上传与更新

### 个人主页模块
- 展示用户发布的动态
- 展示用户个人信息
- 展示关注/粉丝统计

### 内容发布模块
- 支持文字与图片混合内容发布
- 支持动态转发功能

### 时间线模块
- 关注用户动态流
- 全站热门内容流

### 社交互动模块
- 点赞、取消点赞功能
- 多级评论功能
- 内容转发功能

### 用户关系模块
- 关注/取消关注功能
- 关注列表与粉丝列表

### 通知系统
- 新消息提醒
- 互动提醒（点赞、评论、转发、关注）

## 环境要求

### 开发环境
- **Java**: 17+
- **Maven**: 3.6+
- **Node.js**: 16+
- **MySQL**: 8.0+
- **Redis**: 6.0+

### 部署环境
- **Docker**: 20.10+
- **Docker Compose**: 1.29+

## 项目依赖

### 后端主要依赖
- **Spring Boot**: 3.2.0
- **Spring Security**: 6.2.0
- **JJWT**: 0.12.5
- **Spring Data JPA**: 3.2.0
- **MySQL Connector**: 8.1.0
- **Redis Spring Boot Starter**: 3.2.0
- **Springdoc OpenAPI**: 2.3.0

### 前端主要依赖
- **Vue**: 3.3.11
- **TypeScript**: 5.2.2
- **Pinia**: 2.1.7
- **Vue Router**: 4.2.5
- **Axios**: 1.6.5
- **Element Plus**: 2.4.3
- **Vite**: 5.0.10

## 操作介绍

### 用户注册与登录
1. 访问首页，点击右上角"注册"按钮
2. 填写注册信息（用户名、密码、邮箱、昵称）
3. 点击"注册"按钮，成功后自动跳转到登录页面
4. 输入用户名和密码，点击"登录"按钮
5. 登录成功后跳转到首页

### 发布动态
1. 登录后，在首页或个人主页点击"发布动态"按钮
2. 输入动态内容
3. 点击"添加图片"按钮可以上传图片
4. 点击"发布"按钮，动态发布成功

### 浏览时间线
1. 登录后，首页默认显示关注用户的动态流
2. 点击"热门"标签，可以查看全站热门动态
3. 点击动态卡片可以查看动态详情

### 社交互动
1. **点赞**：点击动态或评论下方的点赞图标
2. **评论**：在动态详情页下方输入评论内容，点击"评论"按钮
3. **转发**：点击动态下方的转发图标，输入转发内容，点击"转发"按钮

### 关注用户
1. 在用户个人主页点击"关注"按钮
2. 在动态列表中点击用户名，进入用户个人主页，点击"关注"按钮
3. 关注成功后，可以在首页看到该用户的动态

### 查看通知
1. 点击顶部导航栏的"通知"图标
2. 查看互动提醒（点赞、评论、转发、关注）
3. 点击通知可以查看详情

### 管理个人资料
1. 点击右上角头像，选择"个人设置"
2. 修改个人信息（昵称、简介、网站、位置）
3. 点击"保存"按钮，修改成功
4. 点击"上传头像"可以更换头像

### 查看关注/粉丝列表
1. 在个人主页点击"关注数"或"粉丝数"
2. 查看关注列表或粉丝列表
3. 点击用户可以进入其个人主页

## 开发环境部署

## 快速开始

### 开发环境部署

#### 1. 配置数据库
```sql
-- 创建数据库
CREATE DATABASE weibo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入初始化SQL脚本
USE weibo;
SOURCE database.sql;
```

#### 2. 启动后端服务
```bash
# 编译打包
mvn clean package -DskipTests

# 启动应用
java -jar target/weibo-backend-1.0.0.jar
```

#### 3. 启动前端服务
```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

#### 4. 访问应用
- **前端应用**: http://localhost:3000
- **后端API**: http://localhost:8080/api
- **API文档**: http://localhost:8080/api/swagger-ui.html

### Docker容器化部署

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## 项目结构

### 后端项目结构
```
weibo-backend/
├── src/
│   ├── main/
│   │   ├── java/com/weibo/
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/       # 控制器层
│   │   │   ├── service/          # 服务层
│   │   │   ├── repository/       # 数据访问层
│   │   │   ├── entity/           # 实体类
│   │   │   ├── dto/              # 数据传输对象
│   │   │   ├── enums/            # 枚举类
│   │   │   ├── exception/        # 异常处理
│   │   │   └── util/             # 工具类
│   │   └── resources/            # 配置资源
│   └── test/                     # 测试代码
├── Dockerfile                    # Docker配置
├── docker-compose.yml            # Docker Compose配置
├── pom.xml                       # Maven依赖
└── README.md                     # 项目说明
```

### 前端项目结构
```
weibo-frontend/
├── src/
│   ├── assets/                   # 静态资源
│   ├── components/               # 公共组件
│   ├── views/                    # 页面组件
│   ├── router/                   # 路由配置
│   ├── stores/                   # 状态管理
│   ├── services/                 # API服务
│   ├── types/                    # TypeScript类型定义
│   ├── utils/                    # 工具函数
│   ├── App.vue                   # 根组件
│   └── main.ts                   # 入口文件
├── index.html                    # HTML模板
├── vite.config.ts                # Vite配置
├── tsconfig.json                 # TypeScript配置
└── package.json                  # 项目依赖
```

## API文档

项目使用Swagger/OpenAPI生成API文档，访问地址：

- **在线文档**: http://localhost:8080/api/swagger-ui.html
- **JSON文档**: http://localhost:8080/api/v3/api-docs

## 安全防护

- **XSS防护**: 实现了XSS过滤器，过滤请求参数中的XSS攻击代码
- **CSRF防护**: 采用JWT Token认证，不依赖Cookie，天然防护CSRF攻击
- **SQL注入防护**: 使用JPA/Hibernate ORM，避免直接拼接SQL语句
- **密码加密**: 使用BCrypt算法对密码进行加密存储
- **敏感数据保护**: 敏感数据在传输和存储过程中进行加密处理

## 性能优化

- **数据库索引**: 合理设计数据库索引，提高查询效率
- **缓存策略**: 使用Redis缓存热点数据，如用户信息、热门动态等
- **异步处理**: 使用消息队列处理非核心业务，提高系统响应速度
- **分库分表**: 支持水平扩展，可根据业务需求进行分库分表

## 开发规范

### 后端开发规范
- **代码风格**: 遵循Java Coding Guidelines
- **命名规范**: 类名使用大驼峰命名，方法名和变量名使用小驼峰命名
- **注释规范**: 关键类、方法和复杂逻辑需要添加详细注释
- **事务管理**: 核心业务逻辑必须添加事务管理
- **异常处理**: 统一的异常处理机制，返回标准化的错误响应

### 前端开发规范
- **代码风格**: 遵循ESLint + Prettier规范
- **组件设计**: 组件化开发，提高组件复用性
- **TypeScript**: 严格使用TypeScript进行类型检查
- **API调用**: 统一的API服务封装，方便维护

## 测试策略

- **单元测试**: 核心业务逻辑测试覆盖率≥80%
- **集成测试**: 测试服务间交互和数据库操作
- **端到端测试**: 测试核心用户流程
- **性能测试**: 测试系统在高并发场景下的表现

## 部署方案

### 单机部署
- 适合开发环境和小规模生产环境
- 直接部署JAR包和Node.js应用

### 容器化部署
- 使用Docker容器化部署，提高部署效率和一致性
- 使用Docker Compose管理多个服务

### 集群部署
- 使用Kubernetes进行容器编排
- 配置负载均衡和自动扩缩容

## 监控与日志

- **日志管理**: 使用SLF4J + Logback记录日志
- **监控告警**: 接入Prometheus + Grafana进行监控
- **链路追踪**: 支持OpenTelemetry进行分布式链路追踪

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 贡献指南

1. Fork本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交变更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开Pull Request

## 联系方式

- **项目主页**: https://github.com/zhl2005zhl/weibo-social-network
- **问题反馈**: https://github.com/zhl2005zhl/weibo-social-network/issues

## 致谢

感谢所有为项目做出贡献的开发者！
