## MINI-FC
基于Docker和Kubernetes,具有最基础功能的函数计算平台,可视作为Serverless架构的一种简易实现。支持在单机环境下实现服务和函数的创建、修改、运行、日志查看等功能。

### Quick Start
1. 配置一台CentOS 7操作系统的服务器。并安装git,jdk8,python2,mysql,tomcat
2. 安装docker和kubernetes
3. 在MySQL中创建数据库,并执行 mini-fc-admin工程的 `ddl.sql`
4. 修改配置文件 `mini-fc-admin`工程的`app.properties`和`db.properties`,`mini-fc-ops`工程的`configs.py`,并在服务器创建相应的目录
5. 修改重启脚本 `mini-fc-admin`和`mini-fc-ops`工程的`build_restart.sh`
6. 执行工程目录下的`build_restart.sh`,通过tomcat和python启动两个工程
7. 访问地址 http://{ip地址}:{端口号}/console

### 项目组件

#### mini-fc-admin
技术栈: java,mysql,nutz,jquery,bootstrap  
函数计算的控制台。实现了对服务、函数、触发器的配置功能，根据需要触发mini-fc-ops中相应的操作，提供对于函数执行的代理接口。

#### mini-fc-ops
技术栈: python,docker,py-docker,wsgi,kubernetes  
函数计算的运维接口。根据函数信息，构建成可运行http server的docker容器。通过docker和kubernetes部署服务，并提供了状态查询和滚动更新的功能。

#### mini-fc-runtime-java
函数计算的Java运行环境

#### mini-fc-examples
可在函数计算中运行的示例程序。包括了Java和Python的单文件版,Python的zip包版,Java的Jar包版

#### mini-fc-cli
(规划中)通过shell终端执行函数的创建、修改与执行 

### 项目目标

#### 具备基本的容器化和自动运维能力
- [x] 代码提交后创建/更新 docker 镜像 -- 已完成
- [x] 调用函数时启动容器
- [x] 容器运行状态检测
- [ ] 长时间空闲时停止容器(可选)
- [ ] 根据调用量自动扩容(可选)

#### 实现语言解释器和运行环境
- [x] 支持 python2.7
- [x] 支持 Java8
- [ ] 支持 nodejs8 (可选)
- [ ] 保存代码时语法基本检测(可选)
- [ ] 控制台运行函数

#### 实现函数触发器功能
- [ ] 支持HTTP触发器
- [ ] 支持消息事件触发器(可选)
- [ ] 支持定时触发器(可选)

#### 支持shell终端操作
- [ ] 编写可执行的shell命令集
- [ ] 解释命令并在服务器上执行