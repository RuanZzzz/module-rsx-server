# module-rsx-server

这是当前学习项目的第一个最小 Spring Boot 服务端骨架。

## 当前目标

- 能本地启动
- 有一个最简单的接口
- 为后续容器化做准备
- 支持登录、业务管理接口和图片上传

## 运行方式

先确保当前终端已切到 Java 17：

```bash
usejava17
```

然后执行：

```bash
cd /Users/ruanshaoxiang/Desktop/project/app/module-rsx-server
mvn spring-boot:run
```

访问：

```text
http://localhost:8082/hello
```

## 图片上传

上传接口：

```text
POST /api/files/upload
```

要求：

- 请求头带 `X-Token`
- 表单字段名为 `file`
- 支持 `jpg / jpeg / png / gif / webp`
- 单文件最大 `5MB`

当前设计：

- 文件保存到本地 `uploads/` 目录
- 数据库 `file_resource` 表保存文件名、访问 URL、类型和大小
- 浏览器通过 `/uploads/...` 访问图片
- 后续容器化时，需要把 `uploads/` 挂载成宿主机目录或 Docker volume

## Docker Compose 运行

本地 IDEA 启动时，后端访问：

```text
http://localhost:8082
```

Docker Compose 启动时，为了避免和 IDEA 的 `8082` 冲突，本机访问端口改成：

```text
http://localhost:8083
```

启动：

```bash
docker compose up --build
```

停止：

```bash
docker compose down
```

如果需要连 Compose 里的 MySQL：

```text
localhost:3307
```

当前 Compose 编排了三个服务：

- `mysql`
  使用 `mysql:8.4`，数据保存到 Docker volume `mysql_data`

- `redis`
  使用 `redis:7`，数据保存到 Docker volume `redis_data`，后续用于学习缓存和多 Pod 登录态共享

- `server`
  使用当前项目的 `Dockerfile` 构建镜像，上传目录挂载到宿主机 `./uploads`，通过服务名 `mysql` 和 `redis` 访问依赖服务

关键点：

- IDEA 模式：Java 进程直接写项目目录下的 `uploads/`
- Compose 模式：容器内写 `/app/uploads`，但通过挂载落到宿主机 `./uploads`
- 两种模式最终都能在项目目录看到上传文件
- `docker compose down` 不会删除 `mysql_data` volume
- `docker compose down -v` 会删除 MySQL 和 Redis volume，对应数据会被清掉

验证 Redis：

```bash
docker exec module-rsx-compose-redis redis-cli ping
```

正常返回：

```text
PONG
```

## Redis 验证接口

当前已经接入 Spring Boot Redis 客户端，用于验证后端服务是否能通过 Compose 服务名访问 Redis。

这些接口属于 `/api/**`，所以需要先登录并在请求头带 `X-Token`。

验证 Spring Boot 到 Redis 的连通性：

```text
GET /api/redis/ping
```

写入一个 10 分钟过期的演示值：

```text
POST /api/redis/demo?value=hello-redis
```

读取演示值：

```text
GET /api/redis/demo
```

也可以直接进入 Redis 容器验证真实数据：

```bash
docker exec module-rsx-compose-redis redis-cli get module-rsx:demo
docker exec module-rsx-compose-redis redis-cli ttl module-rsx:demo
```

## 登录态 Redis 存储

当前登录 token 已经从 JVM 内存迁移到 Redis。

登录后会写入：

```text
module-rsx:auth:token:{token}
```

这个 key 保存当前登录用户信息，并设置默认 120 分钟过期时间。每次带有效 `X-Token` 访问受保护接口时，后端会刷新这个 token 的过期时间。

验证登录 token 是否在 Redis：

```bash
docker exec module-rsx-compose-redis redis-cli keys 'module-rsx:auth:token:*'
docker exec module-rsx-compose-redis redis-cli ttl 'module-rsx:auth:token:你的token'
```

登出后，对应 Redis key 会被删除，再使用旧 token 访问接口会返回：

```text
login expired or invalid token
```

这一步解决的是多实例问题：以后多个后端 Pod 同时运行时，它们不再依赖各自 JVM 内存，而是统一从 Redis 查询登录态。

## 目录规范

后续服务端目录结构按这里约定推进：

- [docs/server-structure.md](/Users/ruanshaoxiang/Desktop/project/docs/server-structure.md)
