FROM daocloud.io/java:8
MAINTAINER "g4seek <g4seek@gmail.com>"
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
COPY server.jar /usr/local/server.jar
EXPOSE 8000
CMD ["java", "-cp", "/usr/local/server.jar", "Server_${RANDOM}"]