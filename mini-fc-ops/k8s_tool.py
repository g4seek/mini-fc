# coding=utf-8
import os

import common_util
import configs
from common_util import logger


# 运行服务并暴露端口
def run_service(function_info):
    image_name_with_version = common_util.get_image_name_with_version(function_info)
    k8s_service_name = get_k8s_service_name(function_info)
    # TODO 查询状态决定是否执行命令
    cmd = 'kubectl run {0} --image={1}/{2} --port={3} --env="TZ=Asia/Shanghai" -n {4}'.format(
        k8s_service_name, configs.docker_registry, image_name_with_version, 8000, configs.k8s_namespace)
    common_util.execute_print_cmd(cmd)
    cmd = 'kubectl expose deploy {0} --type=NodePort -n {1}'.format(
        k8s_service_name, configs.k8s_namespace)
    common_util.execute_print_cmd(cmd)
    cmd = 'kubectl set image deploy/{0} {1}={2}/{3} -n {4}'.format(
        k8s_service_name, k8s_service_name, configs.docker_registry, image_name_with_version, configs.k8s_namespace)
    common_util.execute_print_cmd(cmd)
    return 'http://{0}:{1}/api/v1/namespaces/{2}/services/{3}/proxy/'.format(
        configs.mini_fc_host, configs.kubectl_proxy_port, configs.k8s_namespace, k8s_service_name)


# 删除服务
def delete_service(function_info):
    k8s_service_name = get_k8s_service_name(function_info)
    cmd = 'kubectl delete svc {0} -n {1}'.format(k8s_service_name, configs.k8s_namespace)
    common_util.execute_print_cmd(cmd)
    cmd = 'kubectl delete deploy {0} -n {1}'.format(k8s_service_name, configs.k8s_namespace)
    common_util.execute_print_cmd(cmd)
    return "ok"


# 获取运行日志
def get_pod_log(function_info):
    k8s_service_name = get_k8s_service_name(function_info)
    pod_names = get_pod_names(k8s_service_name)
    result = ""
    for pod_name in pod_names:
        pod_name = pod_name.replace("\n", "")
        cmd = 'kubectl logs {0} -n {1} --tail 20'.format(pod_name, configs.k8s_namespace)
        logger.info(cmd)
        lines = os.popen(cmd, "r").readlines()
        result += "=" * 10 + pod_name.replace("\n", "") + "=" * 10 + "\n"
        for line in lines:
            result += line
        result += "\n"
    return result


# 获取k8s运行状况
def get_k8s_overview(type):
    result_list = []
    if type == "pod":
        cmd = 'kubectl get po -o wide -n {0}'.format(configs.k8s_namespace)
        lines = os.popen(cmd, "r").readlines()
        for i in range(1, len(lines)):
            result = ' '.join(lines[i].split()).split(' ')
            result_list.append(
                {"name": result[0], "ready": result[1], "status": result[2], "restarts": result[3], "age": result[4],
                 "ip": result[5], "node": result[6]})
    elif type == "service":
        cmd = 'kubectl get svc -o wide -n {0}'.format(configs.k8s_namespace)
        lines = os.popen(cmd, "r").readlines()
        for i in range(1, len(lines)):
            result = ' '.join(lines[i].split()).split(' ')
            result_list.append(
                {"name": result[0], "type": result[1], "clusterIp": result[2], "externalIp": result[3],
                 "ports": result[4], "age": result[5], "selector": result[6]})
    elif type == "deployment":
        cmd = 'kubectl get deploy -o wide -n {0}'.format(configs.k8s_namespace)
        lines = os.popen(cmd, "r").readlines()
        for i in range(1, len(lines)):
            result = ' '.join(lines[i].split()).split(' ')
            result_list.append(
                {"name": result[0], "ready": result[1], "upToDate": result[2], "available": result[3],
                 "age": result[4], "containers": result[5], "images": result[6], "selector": result[7]})
    return result_list


# 对部署进行扩容或缩容
def scale_deployment(target_num, deployment_name):
    cmd = 'kubectl scale deploy {0} --replicas={1} -n {2}'.format(deployment_name, target_num, configs.k8s_namespace)
    common_util.execute_print_cmd(cmd)
    return "ok"


# 获取k8s服务名称
def get_k8s_service_name(function_info):
    return "{0}-{1}".format(function_info.service_name, function_info.function_name)


# 获取容器组名称
def get_pod_names(k8s_service_name):
    cmd = 'kubectl get po -o=custom-columns=NAME:.metadata.name -n {0} | grep {1}-'.format(
        configs.k8s_namespace, k8s_service_name)
    logger.info(cmd)
    return os.popen(cmd, "r").readlines()
