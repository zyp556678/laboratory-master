

# 实验室管理系统


## 项目介绍

该项目是基于 SpringBoot 开发的张宇鹏的实验室管理系统，采用前后端分离架构模式。为了管理和协调实验室资源、设备、人员和实验室活动而设计的软件系统。该系统旨在提高实验室的效率、安全性和管理水平，同时简化日常操作并提供实验室数据的准确记录和分析。

## 主要功能

### 用户管理模块
- **角色权限**: 系统包含管理员、教师、学生三种角色，分别配有不同权限
- **用户操作**: 用户登陆、注册、修改密码、修改个人信息、头像上传
- **管理员功能**: 查看、新增、修改、删除用户，重置密码

### 日志管理模块
- **登录日志**: 查看、记录、删除、导出登录日志
- **操作日志**: 查看、记录、删除、导出操作日志

### 实验室管理模块
- **实验室管理**: 查看、新增、修改、删除实验室，导入导出，预约功能
- **分室管理**: 实验室分室的信息管理
- **预约管理**: 预约信息查看、取消、删除、审核、导出
- **开放管理**: 开放信息同步、修改、重置

### 课程与人员管理
- **课程管理**: 实验室课程信息管理
- **课程项目**: 课程项目信息管理
- **人员管理**: 实验室人员信息管理

### 设备管理模块
- **设备管理**: 设备查看、新增、编辑、删除、导入导出
- **设备报修**: 报修记录查看、上报故障、修改信息、删除记录

## 技术栈

### 前端技术栈
| 技术 | 说明 |
|------|------|
| Vue | 前端核心框架，用于构建单页应用 |
| VueX | 状态管理，用于全局状态共享 |
| Vue Router | 路由控制，实现页面跳转 |
| Axios | HTTP 请求框架 |
| Element-UI | UI 组件库 |
| ECharts | 可视化图表 |
| Vite | 构建工具 |

### 后端技术栈
| 技术 | 说明 |
|------|------|
| SpringBoot | 快速构建微服务框架 |
| MybatisPlus | ORM 框架 |
| Pagehelper | 分页插件 |
| JWT | JSON Web Token |
| EasyExcel | 导入导出框架 |
| Swagger2 | 接口文档 |
| MySQL | 主数据库 |
| Minio | 文件存储 |

## 环境要求

- MySQL 5.7+
- JDK 1.8+
- Maven 3.6+
- NodeJS 8.9+

## 快速开始

### 1. 克隆项目
```bash
git clone https://gitee.com/zyp556678/laboratory.git
```

### 2. 配置数据库
- 使用数据库连接工具创建数据库: `CREATE DATABASE laboratorydb;`
- 导入初始化脚本 `laboratorydb-init.sql`

### 3. 运行后端
- 使用 IDE 打开后端项目 `laboratory-server`
- 使用 Maven 下载项目依赖
- 编辑 `application-local.yml` 文件，修改数据库连接信息
- 运行 `LaboratoryApplication.java`
- 接口文档地址: http://localhost:8070/doc.html

### 4. 运行前端
```bash
cd laboratory-admin
npm install
npm run dev
```
- 浏览器访问: http://localhost:9528

## 项目结构

```
laboratory/
├── laboratory-admin/          # 前端项目
│   ├── src/
│   │   ├── api/               # API 接口
│   │   ├── components/        # 公共组件
│   │   ├── layout/            # 布局组件
│   │   ├── router/            # 路由配置
│   │   ├── store/             # 状态管理
│   │   ├── utils/             # 工具函数
│   │   └── views/             # 页面视图
│   └── ...
└── laboratory-server/          # 后端项目
    └── src/main/java/com/laboratory/
        ├── config/            # 配置类
        ├── controller/        # 控制器
        ├── core/              # 核心模块
        ├── mapper/            # 数据访问层
        ├── model/             # 数据模型
        └── service/           # 业务逻辑层
```

## Docker 部署

项目提供了 Docker 配置文件，支持容器化部署：

```bash
# 使用 docker-compose 启动完整服务
cd docs/script
docker-compose up -d
```

## 感谢

- [花裤衩](https://gitee.com/panjiachen/vue-admin-template?_from=gitee_search) 开源框架

## 交流讨论

欢迎一起探讨，如果你觉得还可以，可以给我点一个 star

## 许可证

本项目遵循开源协议，具体协议请参考项目中的 LICENSE 文件。