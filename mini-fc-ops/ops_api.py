# coding=utf-8
from BaseHTTPServer import BaseHTTPRequestHandler
from BaseHTTPServer import HTTPServer
from SocketServer import ThreadingMixIn

import requests

import admin_proxy
import configs
import docker_tool
import k8s_tool
from logger_util import logger


class MultiThreadedHTTPServer(ThreadingMixIn, HTTPServer):
    pass


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


# 通过代理访问方法
def function_proxy(param_map):
    path = param_map["path"]
    service_name = param_map["serviceName"]
    function_name = param_map["functionName"]
    del param_map["serviceName"]
    del param_map["functionName"]
    del param_map["path"]
    if not path:
        path = "/"
    container_name = "minifc_{0}_{1}".format(service_name, function_name)
    execute_port = docker_tool.get_run_port(container_name)
    execute_url = "http://{0}:{1}/{2}".format(configs.mini_fc_host, execute_port, path)
    if not param_map:
        execute_url += "?"
        for key in param_map.keys():
            execute_url += key
            execute_url += "="
            execute_url += param_map[key]
            execute_url += "&"
    return requests.get(execute_url).content


# 通过代理访问k8s服务
def k8s_function_proxy(param_map):
    service = param_map["service"]
    path = param_map["path"]
    if not path:
        path = "/"
    del param_map["service"]
    del param_map["path"]
    execute_url = "http://127.0.0.1:{0}/api/v1/namespaces/default/services/minifc-python-test-hello-k8s/proxy/".format(
        configs.kubectl_proxy_port)
    if not param_map:
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
        uri = self.path
        param_map = {}
        # 从uri中提取参数
        if self.path.find("?") > 0:
            question_index = self.path.index("?")
            uri = self.path[:question_index]
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
            elif uri == "/functionProxy":
                result = function_proxy(param_map)
            elif uri == "/getExecuteLog":
                function_id = param_map["functionId"]
                result = get_execute_log(function_id)
            elif uri == "/executeK8sFunction":
                function_id = param_map["functionId"]
                result = execute_k8s_function(function_id)
            elif uri == "/getK8sExecuteLog":
                function_id = param_map["functionId"]
                result = get_k8s_execute_log(function_id)
            elif uri == "/k8sFunctionProxy":
                result = k8s_function_proxy(param_map)
        except Exception as e:
            result = e.message
        logger.info("result:" + str(result))
        self.wfile.write(result)


if __name__ == '__main__':
    ip = '0.0.0.0'
    port = int(configs.mini_fc_ops_port)
    server = MultiThreadedHTTPServer((ip, port), OpsApiHandler)
    logger.info("Starting server, use <Ctrl-C> to stop.ip:" + ip + ",port:" + str(port))
    server.serve_forever()
