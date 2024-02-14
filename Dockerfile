FROM openjdk:8-jdk-alpine

WORKDIR /app

# 将本地的JAR文件复制到容器的/app目录下
COPY target/WeChatBc-1.0-SNAPSHOT-jar-with-dependencies.jar /app/WeChatBc-1.0-SNAPSHOT.jar
COPY src/main/resources/log4j2.xml /config/log4j2.xml
CMD ["java", "-Dlog4j.configurationFile=/config/log4j2.xml", "-jar", "WeChatBc-1.0-SNAPSHOT.jar"]
