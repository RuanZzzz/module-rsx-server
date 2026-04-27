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

当前 Compose 编排了两个服务：

- `mysql`
  使用 `mysql:8.4`，数据保存到 Docker volume `mysql_data`

- `server`
  使用当前项目的 `Dockerfile` 构建镜像，上传目录挂载到宿主机 `./uploads`

关键点：

- IDEA 模式：Java 进程直接写项目目录下的 `uploads/`
- Compose 模式：容器内写 `/app/uploads`，但通过挂载落到宿主机 `./uploads`
- 两种模式最终都能在项目目录看到上传文件
- `docker compose down` 不会删除 `mysql_data` volume
- `docker compose down -v` 会删除 MySQL volume，数据库数据会被清掉

## 目录规范

后续服务端目录结构按这里约定推进：

- [docs/server-structure.md](/Users/ruanshaoxiang/Desktop/project/docs/server-structure.md)
