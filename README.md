# LostFound_Office 🏫

小学期项目 —— 失物招领管理系统

> 一个基于 Spring Boot + Vue 3 的前后端分离失物招领平台，支持用户发布失物/拾物信息、认领物品、评论交流，并提供管理后台进行审核与数据管理。

---

## 📋 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [系统功能](#系统功能)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
  - [环境要求](#环境要求)
  - [后端启动](#后端启动)
  - [前端启动](#前端启动)
- [接口文档](#接口文档)
- [管理后台](#管理后台)
- [开发计划](#开发计划)
- [许可证](#许可证)

---

## 📖 项目简介

**LostFound_Office** 是一个面向校园/办公场景的失物招领系统，主要解决物品丢失后信息不对称的问题。

用户可以在平台上发布**丢失物品**和**拾到物品**的信息，通过认领和评论功能实现失主与拾主之间的沟通，管理员可以在后台审核和管理所有信息。

### 核心业务流程

```
用户发布失物/拾物 → 信息审核 → 双方匹配 → 认领申请 → 确认归还 → 完成
```

---

## 🛠️ 技术栈

### 后端技术

| 技术 | 说明 |
|------|------|
| **Spring Boot 2.x** | 项目基础框架 |
| **MyBatis-Plus** | ORM 持久层框架，简化数据库操作 |
| **MySQL** | 关系型数据库 |
| **Maven** | 项目构建与依赖管理 |
| **JWT (jjwt)** | 用户身份认证与 Token 校验 |
| **Lombok** | 实体类简化（@Data、@Builder 等） |

### 前端技术

| 技术 | 说明 |
|------|------|
| **Vue 3** | 前端渐进式框架 |
| **Vite** | 前端构建与开发服务器 |
| **Pinia** | 状态管理（代替 Vuex） |
| **Vue Router 4** | 前端路由管理 |
| **Element Plus** | UI 组件库 |
| **Axios** | HTTP 请求封装 |
| **ECharts** | 数据可视化（管理后台看板） |

---

## ✨ 系统功能

### 👤 用户端

| 功能 | 描述 |
|------|------|
| **用户注册 / 登录** | 支持账号密码注册登录，JWT 鉴权 |
| **失物发布** | 填写物品名称、丢失地点、时间、描述、图片等 |
| **拾物发布** | 填写拾取地点、时间、物品描述、图片等 |
| **失物广场** | 浏览所有丢失物品信息 |
| **拾物广场** | 浏览所有拾到物品信息 |
| **物品搜索** | 按关键词搜索物品 |
| **物品详情** | 查看物品详细信息 |
| **认领物品** | 失主发起认领申请，拾主确认归还 |
| **评论留言** | 在物品详情页留言交流 |
| **个人中心** | 管理自己的发布记录、认领记录 |
| **图片上传** | 支持上传物品图片 |

### 🛡️ 管理员端

| 功能 | 描述 |
|------|------|
| 📊 **数据看板** | ECharts 可视化统计（发布量、认领量等） |
| ✅ **信息审核** | 审核用户发布的失物/拾物信息 |
| 👥 **用户管理** | 查看和管理用户账号 |
| 📁 **物品管理** | 管理所有失物/拾物记录 |

---

## 📁 项目结构

### 后端结构

```
lost-and-found-api/
├── src/main/java/com/lostfound/
│   ├── common/              # 通用工具类
│   │   └── Result.java      # 统一 API 响应封装
│   ├── config/              # 配置类
│   │   ├── CorsConfig.java       # 跨域配置
│   │   ├── JwtInterceptor.java   # JWT 拦截器
│   │   ├── MyMetaObjectHandler.java  # MyBatis-Plus 自动填充
│   │   └── WebMvcConfig.java     # Web MVC 配置
│   ├── controller/          # 控制器层（API 接口）
│   │   ├── AdminController.java     # 管理员接口
│   │   ├── ClaimController.java     # 认领接口
│   │   ├── CommentController.java   # 评论接口
│   │   ├── FileUploadController.java # 文件上传接口
│   │   ├── FoundItemController.java  # 拾物接口
│   │   ├── LostItemController.java   # 失物接口
│   │   └── UserController.java       # 用户接口
│   ├── dto/                 # 数据传输对象
│   │   ├── LoginDto.java
│   │   ├── RegisterDto.java
│   │   └── PageDto.java
│   ├── entity/              # 实体类
│   │   ├── User.java
│   │   ├── LostItem.java
│   │   ├── FoundItem.java
│   │   ├── Claim.java
│   │   └── Comment.java
│   ├── exception/           # 全局异常处理
│   │   └── GlobalExceptionHandler.java
│   ├── mapper/              # MyBatis-Plus Mapper 接口
│   ├── service/             # 业务逻辑层
│   │   ├── impl/            # 业务实现类
│   │   └── ...Service.java  # 业务接口
│   └── utils/               # 工具类
│       ├── JwtUtils.java        # JWT 工具
│       └── PasswordUtils.java   # 密码加密工具
├── src/main/resources/
│   ├── application.yml      # 应用配置文件
│   └── init.sql             # 数据库初始化脚本
├── uploads/                 # 文件上传目录
├── pom.xml                  # Maven 依赖配置
└── target/                  # 编译输出（已忽略）
```

### 前端结构

```
lost-and-found-web/
├── public/                  # 静态资源
├── src/
│   ├── api/                 # API 接口模块
│   │   ├── admin.js         # 管理员 API
│   │   ├── claim.js         # 认领 API
│   │   ├── comment.js       # 评论 API
│   │   ├── found.js         # 拾物 API
│   │   ├── lost.js          # 失物 API
│   │   ├── upload.js        # 上传 API
│   │   └── user.js          # 用户 API
│   ├── layout/              # 布局组件
│   │   ├── Layout.vue       # 用户端布局
│   │   └── AdminLayout.vue  # 管理端布局
│   ├── router/              # 路由配置
│   │   └── index.js
│   ├── stores/              # Pinia 状态管理
│   │   └── user.js
│   ├── utils/               # 工具类
│   │   └── axios.js         # Axios 封装（拦截器、Token）
│   └── views/               # 页面组件
│       ├── Home/            # 首页
│       ├── Login/           # 登录页
│       ├── Register/        # 注册页
│       ├── Lost/            # 失物相关（列表/详情/发布）
│       ├── Found/           # 拾物相关（列表/详情/发布）
│       ├── Profile/         # 个人中心
│       ├── UserProfile/     # 用户主页
│       └── Admin/           # 管理后台（看板/审核/用户管理/物品管理）
├── index.html               # 入口 HTML
├── vite.config.js           # Vite 配置（含代理）
├── package.json             # 前端依赖
└── dist/                    # 打包输出（已忽略）
```

---

## 🚀 快速开始

### 环境要求

| 工具 | 版本要求 |
|------|---------|
| JDK | 8 或 11+ |
| Maven | 3.6+ |
| MySQL | 5.7+ 或 8.0+ |
| Node.js | 16+ |
| npm / yarn | 任意 |

### 后端启动

**1. 创建数据库**

打开 MySQL，执行项目中的建表脚本：

```sql
source D:\IJproject\LostFound_Office\lost-and-found-api\src\main\resources\init.sql
```

或者在 MySQL 中手动创建一个名为 `lostfound` 的数据库，然后运行脚本。

**2. 修改数据库配置**

打开 `lost-and-found-api/src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lostfound?useUnicode=true&characterEncoding=utf-8
    username: root          # 改成你的 MySQL 用户名
    password: 123456        # 改成你的 MySQL 密码
```

**3. 启动后端**

```bash
cd lost-and-found-api
mvn spring-boot:run
```

启动成功后，后端运行在 `http://localhost:8080`。

### 前端启动

```bash
cd lost-and-found-web
npm install
npm run dev
```

启动成功后，访问 `http://localhost:5173` 即可看到页面。

> 注意：前端通过 Vite 的 proxy 配置将 `/api` 请求代理到后端 `http://localhost:8080`，所以开发时无需修改 API 地址。

---

## 📡 接口概览

| 模块 | 接口路径 | 说明 |
|------|---------|------|
| **用户** | `/api/user/register` | 用户注册 |
| | `/api/user/login` | 用户登录 |
| | `/api/user/info` | 获取用户信息 |
| **失物** | `/api/lost/page` | 失物列表分页查询 |
| | `/api/lost/detail/{id}` | 失物详情 |
| | `/api/lost/add` | 发布失物 |
| **拾物** | `/api/found/page` | 拾物列表分页查询 |
| | `/api/found/detail/{id}` | 拾物详情 |
| | `/api/found/add` | 发布拾物 |
| **认领** | `/api/claim/add` | 发起认领申请 |
| | `/api/claim/list` | 认领记录列表 |
| **评论** | `/api/comment/list` | 获取评论列表 |
| | `/api/comment/add` | 添加评论 |
| **管理后台** | `/api/admin/**` | 管理员相关接口 |
| **文件上传** | `/api/file/upload` | 上传图片文件 |

---

## 🔐 管理后台

部署后访问 `http://localhost:5173/admin` 进入管理后台，需要使用管理员账号登录。

你可以在数据库中手动将某个用户的 `role` 字段设置为 `admin` 以获取管理员权限。

管理后台功能：
- 📊 **数据看板** — 用户数、物品发布量、认领量等统计图表
- ✅ **内容审核** — 审核发布的失物/拾物信息
- 👥 **用户管理** — 查看、禁用用户账号
- 📁 **物品管理** — 管理所有物品信息

---

## 🗺️ 开发计划

- [x] 用户注册登录
- [x] 失物发布与展示
- [x] 拾物发布与展示
- [x] 认领功能
- [x] 评论功能
- [x] 图片上传
- [x] 管理后台
- [ ] 消息通知（站内信）
- [ ] 邮箱/短信验证码注册
- [ ] 微信小程序端

---

## 📄 许可证

本项目仅用于**小学期课程学习**，仅供个人学习参考使用。

---

## 👨‍💻 作者

- **GitHub**: [Hayden0226](https://github.com/Hayden0226)
- **项目地址**: [https://github.com/Hayden0226/LostFound_Office](https://github.com/Hayden0226/LostFound_Office)

---

> 如果这个项目对你有帮助，欢迎点个 ⭐ Star 支持一下喵~
