# coding=utf-8
import os

import configs
from logger_util import logger


# 运行服务并暴露端口
def run_service(function_info):
    image_name = get_image_name(function_info)
    k8s_service_name = get_k8s_service_name(function_info)
    cmd = "kubectl run {0} --image={1}/{2} --port={3}".format(k8s_service_name, configs.docker_registry, image_name,
                                                              8000)
    execute_print_cmd(cmd)
    # TODO 部署有问题,无法暴露接口到宿主机
    # cmd = 'kubectl expose deployment/{0} --type="NodePort" --port {1}'.format(k8s_service_name, 8000)
    # execute_print_cmd(cmd)

    proxy_url = 'http://{0}:{1}/k8sFunctionProxy?path=/&service={2}'.format(
        configs.mini_fc_host, configs.mini_fc_ops_port, k8s_service_name)
    return proxy_url


# 删除服务
def remove_image(function_info):
    k8s_service_name = get_k8s_service_name(function_info)
    cmd = "kubectl delete svc " + k8s_service_name
    execute_print_cmd(cmd)
    cmd = "kubectl delete deploy " + k8s_service_name
    execute_print_cmd(cmd)
    return "ok"


# 获取运行日志
def get_pod_log(function_info):
    k8s_service_name = get_k8s_service_name(function_info)
    pod_name = get_pod_name(k8s_service_name)
    cmd = "kubectl log " + pod_name
    logger.info(cmd)
    lines = os.popen(cmd, "r").readlines()
    result = ""
    for line in lines:
        result += line
    return result


# 获取镜像名称
def get_image_name(function_info):
    return "minifc_{0}_{1}".format(function_info.service_name, function_info.function_name)


# 获取k8s服务名称
def get_k8s_service_name(function_info):
    return "minifc-{0}-{1}".format(function_info.service_name, function_info.function_name)


# 获取容器组名称
def get_pod_name(k8s_service_name):
    cmd = "kubectl get pods -o=custom-columns=NAME:.metadata.name | grep '{0}-'".format(k8s_service_name)
    logger.info(cmd)
    return os.popen(cmd, "r").readline()


def execute_print_cmd(cmd):
    logger.info(cmd)
    os.system(cmd)
