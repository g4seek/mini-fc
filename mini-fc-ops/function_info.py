# coding=utf-8
class FunctionInfo:

    def __init__(self, function_info):
        # 服务名称
        self.service_name = str(function_info["serviceName"]).encode('utf-8')
        # 函数名称
        self.function_name = str(function_info["functionName"]).encode('utf-8')
        # 函数入口
        function_entrance = str(function_info["functionEntrance"]).encode('utf-8')
        # 函数文件名
        self.file_name = function_entrance.split(".")[0]
        # 函数方法名
        self.method_name = function_entrance.split(".")[1]
        # 源代码
        self.source_code = str(function_info["sourceCode"]).encode('utf-8')
