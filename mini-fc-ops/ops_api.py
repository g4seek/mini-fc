# coding=utf-8
import traceback
from BaseHTTPServer import BaseHTTPRequestHandler
from BaseHTTPServer import HTTPServer
from SocketServer import ThreadingMixIn

import requests

import admin_proxy
import configs
import docker_tool
import k8s_tool
from logger_util import logger


# 创建函数
def create_function(function_id):
    function_info = admin_proxy.get_function(function_id)
    return docker_tool.build_image(function_info)


# 执行函数
def execute_function(function_id):
    function_info = admin_proxy.get_function(function_id)
    return docker_tool.run_container(function_info)


# 删除函数
def delete_function(function_id):
    function_info = admin_proxy.get_function(function_id)
    docker_tool.remove_image(function_info)
    return k8s_tool.delete_service(function_info)


# 获取执行日志
def get_execute_log(function_id):
    function_info = admin_proxy.get_function(function_id)
    return docker_tool.get_container_log(function_info)


# 通过k8s执行函数
def execute_k8s_function(function_id):
    function_info = admin_proxy.get_function(function_id)
    return k8s_tool.run_service(function_info)


# 获取k8s执行日志
def get_k8s_execute_log(function_id):
    function_info = admin_proxy.get_function(function_id)
    return k8s_tool.get_pod_log(function_info)


# 获取k8s运行状况
def get_k8s_overview(type):
    return k8s_tool.get_k8s_overview(type)


# 对部署进行扩容或缩容
def scale_deployment(target_num, deployment_name):
    return k8s_tool.scale_deployment(target_num, deployment_name)


# 通过代理访问方法
def function_proxy(uri, param_map):
    if uri.count("/") < 4:
        return "Invalid uri. should be: /proxy/${serviceName}/${functionName}/${path}"
    first_slash = uri.find("/")
    second_slash = uri.find("/", first_slash + 1)
    third_slash = uri.find("/", second_slash + 1)
    fourth_slash = uri.find("/", third_slash + 1)
    service_name = uri[second_slash + 1: third_slash]
    function_name = uri[third_slash + 1: fourth_slash]
    path = uri[fourth_slash + 1:]
    if not path:
        path = ""
    container_name = "minifc_{0}_{1}".format(service_name, function_name)
    execute_port = docker_tool.get_run_port(container_name)
    execute_url = "http://{0}:{1}/{2}".format(configs.mini_fc_host, execute_port, path)
    if param_map:
        execute_url += "?"
        for key in param_map.keys():
            execute_url += key
            execute_url += "="
            execute_url += param_map[key]
            execute_url += "&"
    return requests.get(execute_url).content


# 通过代理访问k8s服务
def k8s_function_proxy(uri, param_map):
    if uri.count("/") < 3:
        return "Invalid uri. should be: /k8sProxy/${service}/${path}"
    first_slash = uri.find("/")
    second_slash = uri.find("/", first_slash + 1)
    third_slash = uri.find("/", second_slash + 1)
    service = uri[second_slash + 1: third_slash]
    path = uri[third_slash + 1:]
    execute_url = "http://127.0.0.1:{0}/api/v1/namespaces/{1}/services/{2}/proxy/{3}".format(
        configs.kubectl_proxy_port, configs.k8s_namespace, service, path)
    if param_map:
        execute_url += "?"
        for key in param_map.keys():
            execute_url += key
            execute_url += "="
            execute_url += param_map[key]
            execute_url += "&"
    return requests.get(execute_url).content


class OpsApiHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()
        uri = str(self.path)
        param_map = {}
        # 从uri中提取参数
        if self.path.find("?") > 0:
            question_index = self.path.index("?")
            uri = str(self.path[:question_index])
            param_pairs = self.path[question_index + 1:].split("&")
            for param_pair in param_pairs:
                key = param_pair.split("=")[0]
                value = param_pair.split("=")[1]
                param_map[key] = value

        # 根据uri映射到对应的方法
        result = None
        try:
            if uri == "/createFunction":
                function_id = param_map["functionId"]
                result = create_function(function_id)
            elif uri == "/executeFunction":
                function_id = param_map["functionId"]
                result = execute_function(function_id)
            elif uri == "/deleteFunction":
                function_id = param_map["functionId"]
                result = delete_function(function_id)
            elif uri == "/getExecuteLog":
                function_id = param_map["functionId"]
                result = get_execute_log(function_id)
            elif uri == "/executeK8sFunction":
                function_id = param_map["functionId"]
                result = execute_k8s_function(function_id)
            elif uri == "/getK8sExecuteLog":
                function_id = param_map["functionId"]
                result = get_k8s_execute_log(function_id)
            elif uri == "/getK8sOverview":
                type = param_map["type"]
                result = get_k8s_overview(type)
            elif uri == "/scaleDeployment":
                target_num = param_map["targetNum"]
                deployment_name = param_map["deploymentName"]
                result = scale_deployment(target_num, deployment_name)
            elif uri.find("/proxy") == 0:
                result = function_proxy(uri, param_map)
            elif uri.find("/k8sProxy") == 0:
                result = k8s_function_proxy(uri, param_map)
        except Exception as e:
            msg = traceback.format_exc()
            logger.error("Exception:" + msg)
            result = e.message
        logger.info("request:{0},response:{1}".format(self.path, result))
        self.wfile.write(result)


class MultiThreadedHTTPServer(ThreadingMixIn, HTTPServer):
    pass


if __name__ == '__main__':
    ip = '0.0.0.0'
    port = int(configs.mini_fc_ops_port)
    server = MultiThreadedHTTPServer((ip, port), OpsApiHandler)
    logger.info("Starting server, use <Ctrl-C> to stop.ip:" + ip + ",port:" + str(port))
    server.serve_forever()
