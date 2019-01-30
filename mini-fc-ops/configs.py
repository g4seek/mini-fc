# coding=utf-8
# minifc部署的服务器
mini_fc_host = "10.216.40.206"
# minifc-ops部署的端口
mini_fc_ops_port = "13000"
# minifc-admin部署的端口
mini_fc_admin_port = "12000"
# kubectl proxy的端口
kubectl_proxy_port = "11000"

# minifc-ops日志路径
log_path = "/home/logs/mini-fc-ops/"
# 构建python镜像使用的dockerfile
docker_file_root_python = "/home/dockerfile/serverless/python"
# 构建java镜像使用的dockerfile
docker_file_root_java = "/home/dockerfile/serverless/java"
# 工程代码目录
project_root = "/home/source/mini-fc/mini-fc-ops"

# docker仓库地址
docker_registry = mini_fc_host + ":5000"
# k8s命名空间
k8s_namespace = "minifc"
