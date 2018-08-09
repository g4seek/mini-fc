#!/bin/bash
echo "拉取代码..."
git pull
echo "安装python依赖..."
pip2 install -r requirements.txt
echo "停止服务..."
ps aux | grep 'ops_api' | grep -v grep | awk '{print $2}' | xargs kill -9
echo "启动服务..."
nohup python2 ops_api.py &