FROM daocloud.io/java:8
MAINTAINER "g4seek <g4seek@gmail.com>"
COPY gson-2.6.2.jar /usr/local/
RUN export CLASSPATH=/usr/local/gson-2.6.2.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
COPY server.jar /usr/local/server.jar
EXPOSE 8000
CMD ["java", "-cp", "/usr/local/gson-2.6.2.jar:/usr/local/server.jar", "Server_${RANDOM}"]