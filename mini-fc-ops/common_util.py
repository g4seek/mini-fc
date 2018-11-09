# coding=utf-8
import logging.handlers
import os

import configs

logger = logging.getLogger('logger')
logger.setLevel(logging.INFO)

log_handler = logging.handlers.TimedRotatingFileHandler(configs.log_path + 'fc-ops.log', when='MIDNIGHT', interval=1,
                                                        backupCount=7)
log_handler.setFormatter(logging.Formatter("%(asctime)s - %(message)s"))
logger.addHandler(log_handler)


# 执行并打印命令
def execute_print_cmd(cmd):
    logger.info(cmd)
    os.system(cmd)


# 获取镜像名称
def get_image_name(function_info):
    return "minifc_{0}_{1}".format(function_info.service_name, function_info.function_name)


# 获取带版本的镜像名称
def get_image_name_with_version(function_info):
    return "minifc_{0}_{1}:v{2}".format(function_info.service_name, function_info.function_name,
                                        function_info.function_version)
