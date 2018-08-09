### 目的
实现一个最简单的函数计算/serverless平台。作为对以后实施servless的探索性项目，并能够作为demo演示函数计算的最基础功能。

### 功能
#### 具备基本的容器化和自动运维能力
- 代码提交后创建/更新 docker 镜像 -- 已完成
- 调用函数时启动容器 -- 已完成
- 长时间空闲时停止容器(可选)
- 容器运行状态检测 -- 已完成
- 根据调用量自动扩容(可选)
#### 实现语言解释器和运行环境
- 支持 python2.7 -- 已完成
- 支持 Java8 -- 开发中
- 支持 nodejs8 (可选)
- 保存代码时语法基本检测(可选)
- 控制台运行函数(可选)
#### 实现函数触发器功能
- 支持HTTP触发器
- 支持定时触发器(可选)
#### 支持通过客户端操作(可选)

### 设计方案
分为以下几个子项目
#### mini-fc-admin
函数计算控制台,用于配置服务,函数,触发器。

#### mini-fc-router
函数访问路由器，用于将函数调用分发到对应的容器中执行。(目前合并到mini-fc-ops中了)

#### mini-fc-ops
自动运维平台。用于代码构建和docker容器相关的操作。

#### mini-fc-cli(可选)
函数计算的客户端。用于通过命令行进行操作。

### 实现步骤
#### 表结构设计
使用MySQL存储函数计算的配置信息。包括服务、函数、触发器的配置。
见 mini-fc-admin/doc/db

#### 开发控制台 
控制台的前后端代码开发。实现通过页面进行服务、函数、触发器配置。提供restful接口供其它服务调用
- 技术栈: java,mysql,nutz,jquery,bootstrap
- git地址: https://git.mail.netease.com/hzlvzimin/mini-fc-admin
- 部署服务器: 10.240.131.51
- tomcat路径: /home/server/tomcat-minifc
- 访问地址: http://10.240.131.51:12000/console

#### 开发运维平台
查询函数的信息和源代码，并构建成可运行http server的docker容器。启动停止容器，转发函数执行请求。
- 技术栈: python,docker,py-docker,wsgi,kubernetes
- git地址 : https://git.mail.netease.com/hzlvzimin/mini-fc-ops
- 部署服务器: 10.240.131.51
- 工程路径: /home/source/mini-fc-ops

### 问题
1. 代码复用性差,多个函数之间无法互相共享代码。
2. python2对unicode的支持不好,需要比较多的额外代码。
3. 日志收集比较困难,目前是直接用docker或k8s的命令获取的。k8s的容器节点是临时性的,关闭后无法再看到日志。
4. python第三方库目前不支持,需要改造dockerfile。
5. 负载均衡和反向代理使用的是k8s自身的功能,没有和consul结合(临时性服务,不固定的端口号)
