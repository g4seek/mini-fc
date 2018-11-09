FROM daocloud.io/java:8
MAINTAINER "hzlvzimin <hzlvzimin@corp.netease.com>"
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
COPY server.jar /usr/local/server.jar
EXPOSE 8000
CMD ["java", "-cp", "/usr/local/server.jar", "Server_${RANDOM}"]