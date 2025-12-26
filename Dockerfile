# 使用官方OpenJDK 17镜像作为基础镜像
FROM openjdk:17.0.1-jdk-slim

# 安装Maven
RUN apt-get update && apt-get install -y maven

# 设置工作目录
WORKDIR /app

# 复制Maven构建文件
COPY pom.xml ./

# 复制源代码
COPY src ./src

# 使用Maven构建项目
RUN mvn package -DskipTests

# 暴露端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "target/weibo-backend-1.0.0.jar"]
