# coding=utf-8
import requests

import configs
from function_info import FunctionInfo

getfunction_uri = "http://{0}:{1}/api/getFunction".format(configs.mini_fc_host, configs.mini_fc_admin_port)


# 调用fc-admin接口查询函数详情
def get_function(function_id):
    result = requests.get("{0}?functionId={1}".format(getfunction_uri, function_id))
    if result.ok:
        function_info = result.json()
        if function_info["code"] != "200":
            return None
        else:
            return FunctionInfo(function_info["data"])
    else:
        return None
