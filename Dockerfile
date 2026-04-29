# 第一阶段：在容器里用 Maven 编译 Spring Boot 项目，生成 jar 包。
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /workspace

# 先复制 pom.xml，可以让 Maven 下载依赖的结果尽量复用 Docker 构建缓存。
COPY pom.xml .

COPY src ./src
# 直接打包项目，同时跳过测试编译，避免镜像构建阶段下载额外的测试插件依赖。
RUN mvn -B -Dmaven.test.skip=true package

# 第二阶段：只保留运行 jar 需要的 JRE，减少最终镜像体积。
FROM eclipse-temurin:17-jre

WORKDIR /app

# 容器内固定使用 /app/uploads，后续通过 Docker volume 或宿主机目录挂载来持久化。
ENV UPLOAD_ROOT_DIR=/app/uploads
# 容器内日志写到 /app/logs，Compose 会把这个目录挂载到宿主机，避免容器重建后日志丢失。
ENV LOG_DIR=/app/logs

COPY --from=builder /workspace/target/module-rsx-server-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
