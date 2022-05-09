FROM openjdk:12.0.2

EXPOSE 8080
#Timezone 설정 변경
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Seoul

ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]