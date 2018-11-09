# coding=utf-8
import fileinput
import os
import random

import common_util
import configs
from common_util import logger


# 生成构建docker镜像需要的文件
def generate_docker_file(function_info):
    docker_file_dir = ""
    if function_info.exec_enviroment == 'Python2.7':
        if function_info.source_code:
            return generate_by_python_source(function_info)
        else:
            return generate_by_python_zip(function_info)
    elif function_info.exec_enviroment == 'Java8':
        if function_info.source_code:
            return generate_by_java_source(function_info)
        else:
            return generate_by_java_jar(function_info)
    return docker_file_dir


# 通过单个python源代码生成
def generate_by_python_source(function_info):
    service_dir = configs.docker_file_root_python + "/" + function_info.service_name + "/"
    docker_file_dir = service_dir + function_info.function_name + "/"
    clear_and_make_docker_dir(service_dir, docker_file_dir)

    # 将模板文件拷贝到指定目录
    command = "cp {0}/template/python2.7/source/Dockerfile {1}".format(configs.project_root, docker_file_dir)
    common_util.execute_print_cmd(command)
    command = "cp {0}/template/python2.7/source/requirements.txt {1}".format(configs.project_root, docker_file_dir)
    common_util.execute_print_cmd(command)
    command = "cp {0}/template/python2.7/source/server.py.tmpl {1}".format(configs.project_root, docker_file_dir)
    common_util.execute_print_cmd(command)

    # 使用函数配置信息替换文件模板中的占位符
    random_id = str(random.randint(10000, 99999))
    docker_file_path = docker_file_dir + "Dockerfile"
    for line in fileinput.input(docker_file_path, inplace=1):
        line = line.replace("${MODULE_NAME}", function_info.file_name).replace("${RANDOM}", random_id)
        print line,
    fileinput.close()

    server_file_path = docker_file_dir + "server.py.tmpl"
    logger.info(server_file_path)
    for line in fileinput.input(server_file_path, inplace=1):
        line = line.replace("${MODULE_NAME}", function_info.file_name).replace("${METHOD_NAME}",
                                                                               function_info.method_name)
        print line,
    fileinput.close()
    # 将函数源代码生成文件,放入构建目录
    source_file = open(docker_file_dir + function_info.file_name + ".py", "w+")
    source_file.write(function_info.source_code)
    source_file.close()
    return docker_file_dir


# 通过python源代码的zip包生成
def generate_by_python_zip(function_info):
    service_dir = configs.docker_file_root_python + "/" + function_info.service_name + "/"
    docker_file_dir = service_dir + function_info.function_name + "/"
    clear_and_make_docker_dir(service_dir, docker_file_dir)

    # 将模板文件拷贝到指定目录
    command = "cp {0}/template/python2.7/zip/Dockerfile {1}".format(configs.project_root, docker_file_dir)
    common_util.execute_print_cmd(command)
    command = "cp {0}/template/python2.7/zip/requirements.txt {1}".format(configs.project_root, docker_file_dir)
    common_util.execute_print_cmd(command)
    command = "cp {0}/template/python2.7/zip/server.py.tmpl {1}".format(configs.project_root, docker_file_dir)
    common_util.execute_print_cmd(command)

    random_id = str(random.randint(10000, 99999))
    upload_file_path = function_info.upload_file_path
    upload_file_name = upload_file_path.rsplit("/", 1)[1]
    upload_file_dir = upload_file_path.rsplit("/", 1)[0]
    # 将上传的文件解压后,复制到dockerfile目录
    command = "mkdir {0}/{1}".format(upload_file_dir, random_id)
    common_util.execute_print_cmd(command)
    command = "cp {0} {1}/{2}".format(upload_file_path, upload_file_dir, random_id)
    common_util.execute_print_cmd(command)
    command = "cd {0}/{1} && unzip {2}".format(upload_file_dir, random_id, upload_file_name)
    common_util.execute_print_cmd(command)
    command = "rm -f {0}/{1}/{2}".format(upload_file_dir, random_id, upload_file_name)
    common_util.execute_print_cmd(command)
    command = "cp -r {0}/{1} {2}".format(upload_file_dir, random_id, docker_file_dir)
    common_util.execute_print_cmd(command)
    command = "rm -rf {0}/{1}".format(upload_file_dir, random_id)
    common_util.execute_print_cmd(command)

    # 使用函数配置信息替换文件模板中的占位符
    docker_file_path = docker_file_dir + "Dockerfile"
    for line in fileinput.input(docker_file_path, inplace=1):
        line = line.replace("${MODULE_NAME}", function_info.file_name).replace("${RANDOM}", random_id)
        print line,
    fileinput.close()
    server_file_path = docker_file_dir + "server.py.tmpl"
    logger.info(server_file_path)
    for line in fileinput.input(server_file_path, inplace=1):
        line = line.replace("${MODULE_NAME}", function_info.file_name).replace("${METHOD_NAME}",
                                                                               function_info.method_name)
        print line,
    fileinput.close()
    # 将函数源代码生成文件,放入构建目录
    source_file = open(docker_file_dir + function_info.file_name + ".py", "w+")
    source_file.write(function_info.source_code)
    source_file.close()
    return docker_file_dir


# 通过单个java源代码生成
def generate_by_java_source(function_info):
    service_dir = configs.docker_file_root_java + "/" + function_info.service_name + "/"
    docker_file_dir = service_dir + function_info.function_name + "/"
    clear_and_make_docker_dir(service_dir, docker_file_dir)

    source_file = open(docker_file_dir + function_info.file_name + ".java", "w+")
    source_file.write(function_info.source_code)
    source_file.close()

    random_id = str(random.randint(10000, 99999))
    # 将模板文件拷贝到指定目录
    command = "cp {0}/template/java8/source/Dockerfile {1}".format(configs.project_root, docker_file_dir)
    common_util.execute_print_cmd(command)
    command = "cp {0}/template/java8/source/Server.java {1}/Server_{2}.java".format(configs.project_root,
                                                                                    docker_file_dir, random_id)
    common_util.execute_print_cmd(command)
    command = "cp {0}/template/java8/source/Handler.java {1}/Handler_{2}.java".format(configs.project_root,
                                                                                      docker_file_dir, random_id)
    common_util.execute_print_cmd(command)
    command = "cp {0}/template/java8/source/gson-2.6.2.jar {1}/gson-2.6.2.jar".format(configs.project_root,
                                                                                      docker_file_dir)
    common_util.execute_print_cmd(command)

    # 使用函数配置信息替换文件模板中的占位符
    docker_file_path = docker_file_dir + "Dockerfile"
    for line in fileinput.input(docker_file_path, inplace=1):
        line = line.replace("${RANDOM}", random_id)
        print line,
    fileinput.close()
    handler_file_path = "{0}/Handler_{1}.java".format(docker_file_dir, random_id)
    for line in fileinput.input(handler_file_path, inplace=1):
        line = line.replace("${MODULE_NAME}", function_info.file_name).replace(
            "${METHOD_NAME}", function_info.method_name).replace("${RANDOM}", random_id)
        print line,
    fileinput.close()
    server_file_path = "{0}/Server_{1}.java".format(docker_file_dir, random_id)
    for line in fileinput.input(server_file_path, inplace=1):
        line = line.replace("${RANDOM}", random_id)
        print line,
    fileinput.close()
    # 编译成jar包
    command = "cd {0} && javac -d ./ ./*.java".format(docker_file_dir)
    common_util.execute_print_cmd(command)
    command = "cd {0} && jar cvf ./server.jar ./*.class".format(docker_file_dir)
    common_util.execute_print_cmd(command)
    return docker_file_dir


# 通过jar包生成
def generate_by_java_jar(function_info):
    service_dir = configs.docker_file_root_java + "/" + function_info.service_name + "/"
    docker_file_dir = service_dir + function_info.function_name + "/"
    clear_and_make_docker_dir(service_dir, docker_file_dir)

    random_id = str(random.randint(10000, 99999))
    # 将模板文件拷贝到指定目录
    command = "cp {0}/template/java8/jar/Dockerfile {1}".format(configs.project_root, docker_file_dir)
    common_util.execute_print_cmd(command)

    upload_file_path = function_info.upload_file_path
    upload_file_name = upload_file_path.rsplit("/", 1)[1]
    upload_file_dir = upload_file_path.rsplit("/", 1)[0]
    command = "mkdir {0}/{1}".format(upload_file_dir, random_id)
    common_util.execute_print_cmd(command)
    command = "cp {0}/template/java8/jar/Server.java {1}/{2}/Server_{2}.java".format(configs.project_root,
                                                                                     upload_file_dir, random_id)
    common_util.execute_print_cmd(command)
    command = "cp {0}/template/java8/jar/Handler.java {1}/{2}/Handler_{2}.java".format(configs.project_root,
                                                                                       upload_file_dir, random_id)
    common_util.execute_print_cmd(command)

    # 使用函数配置信息替换文件模板中的占位符
    docker_file_path = docker_file_dir + "Dockerfile"
    for line in fileinput.input(docker_file_path, inplace=1):
        line = line.replace("${RANDOM}", random_id)
        print line,
    fileinput.close()
    handler_file_path = "{0}/{1}/Handler_{2}.java".format(upload_file_dir, random_id, random_id)
    for line in fileinput.input(handler_file_path, inplace=1):
        line = line.replace("${MODULE_NAME}", function_info.file_name).replace(
            "${METHOD_NAME}", function_info.method_name).replace("${RANDOM}", random_id)
        print line,
    fileinput.close()
    server_file_path = "{0}/{1}/Server_{2}.java".format(upload_file_dir, random_id, random_id)
    for line in fileinput.input(server_file_path, inplace=1):
        line = line.replace("${RANDOM}", random_id)
        print line,
    fileinput.close()

    # 将模板中的Java类编译成class，将上传的jar包解压
    command = "cd {0}/{1} && javac -d ./ ./*.java".format(upload_file_dir, random_id)
    common_util.execute_print_cmd(command)
    command = "cp {0} {1}/{2}".format(upload_file_path, upload_file_dir, random_id)
    common_util.execute_print_cmd(command)
    command = "cd {0}/{1} && jar xvf {2}".format(upload_file_dir, random_id, upload_file_name)
    common_util.execute_print_cmd(command)
    cmd = "rm -rf {0}/{1}/*.jar {0}/{1}/*.java".format(upload_file_dir, random_id)
    common_util.execute_print_cmd(cmd)

    # 编译成jar包,并复制到dockerfile目录
    cmd = "cd {0}/{1} && jar cvf ./server.jar ./".format(upload_file_dir, random_id)
    common_util.execute_print_cmd(cmd)
    cmd = "cp {0}/{1}/server.jar {2}".format(upload_file_dir, random_id, docker_file_dir)
    common_util.execute_print_cmd(cmd)
    cmd = "rm -rf {0}/{1}".format(upload_file_dir, random_id)
    common_util.execute_print_cmd(cmd)
    return docker_file_dir


# 清理并创建目录
def clear_and_make_docker_dir(service_dir, docker_file_dir):
    command = "rm -rf " + docker_file_dir
    common_util.execute_print_cmd(command)
    if not os.path.exists(service_dir):
        os.mkdir(service_dir)
    if not os.path.exists(docker_file_dir):
        os.mkdir(docker_file_dir)
