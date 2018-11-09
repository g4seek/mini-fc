# coding=utf-8
import sys


class FunctionInfo:

    def __init__(self, function_info):
        reload(sys)
        sys.setdefaultencoding('utf-8')
        # 服务名称
        self.service_name = str(function_info["serviceName"])
        # 函数名称
        self.function_name = str(function_info["functionName"])
        # 函数入口
        function_entrance = str(function_info["functionEntrance"])
        # 函数文件名
        self.file_name = function_entrance.split(".")[0]
        # 函数方法名
        self.method_name = function_entrance.split(".")[1]
        # 函数版本
        self.function_version = str(function_info["functionVersion"])
        # 源代码
        self.source_code = str(function_info["sourceCode"])
        # 运行环境
        self.exec_enviroment = str(function_info["execEnviroment"])
        # 上传文件的路径
        self.upload_file_path = str(function_info["uploadFilePath"])
