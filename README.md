# module-rsx-server

这是当前学习项目的第一个最小 Spring Boot 服务端骨架。

## 当前目标

- 能本地启动
- 有一个最简单的接口
- 为后续容器化做准备

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

## 目录规范

后续服务端目录结构按这里约定推进：

- [docs/server-structure.md](/Users/ruanshaoxiang/Desktop/project/docs/server-structure.md)
