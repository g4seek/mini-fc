# coding=utf-8
import fileinput
import os
import random

import docker
import docker.errors

import configs
from logger_util import logger

client = docker.from_env()


# 构建镜像
def build_image(function_info):
    # 生成随机ID,防止文件名重复
    random_id = random.randint(10000, 99999)

    # 创建Dockerfile的目录
    docker_file_dir = ""
    if function_info.exec_enviroment == 'Python2.7':
        service_dir = configs.docker_file_root_python + "/" + function_info.service_name + "/"
        docker_file_dir = service_dir + function_info.function_name + "/"
        command = "rm -rf " + docker_file_dir
        execute_print_cmd(command)
        if not os.path.exists(service_dir):
            os.mkdir(service_dir)
        if not os.path.exists(docker_file_dir):
            os.mkdir(docker_file_dir)

        # 将模板文件拷贝到指定目录
        command = "cp {0}/template/python2.7/Dockerfile {1}".format(configs.project_root, docker_file_dir)
        execute_print_cmd(command)
        command = "cp {0}/template/python2.7/requirements.txt {1}".format(configs.project_root, docker_file_dir)
        execute_print_cmd(command)
        command = "cp {0}/template/python2.7/server.py.tmpl {1}".format(configs.project_root, docker_file_dir)
        execute_print_cmd(command)

        # 使用函数配置信息替换文件模板中的占位符
        docker_file_path = docker_file_dir + "Dockerfile"
        for line in fileinput.input(docker_file_path, inplace=1):
            line = line.replace("${MODULE_NAME}", function_info.file_name).replace("${RANDOM}", str(random_id))
            print line,

        server_file_path = docker_file_dir + "server.py.tmpl"
        logger.info(server_file_path)
        for line in fileinput.input(server_file_path, inplace=1):
            line = line.replace("${MODULE_NAME}", function_info.file_name).replace("${METHOD_NAME}",
                                                                                   function_info.method_name)
            print line,

        # 将函数源代码生成文件,放入构建目录
        source_file = open(docker_file_dir + function_info.file_name + ".py", "w+")
        source_file.write(function_info.source_code)
        source_file.close()
    elif function_info.exec_enviroment == 'Java8':
        pass

    # 构建docker镜像
    image_name = get_image_name(function_info)

    logger.info("building image,image_name:" + image_name)
    client.images.build(tag=image_name, path=docker_file_dir)
    logger.info("image build success,image_name:" + image_name)

    # 添加标签
    tag = "v" + function_info.function_version
    logger.info("tagging image,image_name:{0},tag:{1}".format(image_name, tag))
    client.api.tag(image_name, image_name, tag)
    client.api.tag(image_name, configs.docker_registry + "/" + image_name, tag)
    logger.info("image tag success,image_name:{0},tag:{1}".format(image_name, tag))

    # 推送到仓库
    logger.info("pushing image,image_name:{0},tag:{1}".format(image_name, tag))
    client.images.push(configs.docker_registry + "/" + image_name, tag)
    logger.info("image push success,image_name:{0},tag:{1}".format(image_name, tag))
    return "ok"


# 移除镜像
def remove_image(function_info):
    container_name = get_container_name(function_info)
    try:
        container = client.containers.get(container_name)
        if container:
            logger.info("removing container,container_name:" + container_name)
            client.api.remove_container(container_name, force=True)
            logger.info("container remove success,container_name:" + container_name)
    except:
        pass

    image_name = get_image_name(function_info)
    try:
        image = client.images.get(image_name)
        if image:
            logger.info("removing image,image_name:" + image_name)
            client.images.remove(image=image_name, force=True)
            logger.info("image remove success,image_name:" + image_name)
    except:
        pass

    try:
        image_name = configs.docker_registry + "/" + image_name
        image = client.images.get(image_name)
        if image:
            logger.info("removing image,image_name:" + image_name)
            client.images.remove(image=image_name, force=True)
            logger.info("image remove success,image_name:" + image_name)
    except:
        pass

    cmd = "docker images | grep -E '.*{0}\s+v.*' | awk '{1}' | xargs docker rmi -f".format(image_name, "{print $3}")
    execute_print_cmd(cmd)
    return "ok"


# 运行容器
def run_container(function_info):
    container_name = get_container_name(function_info)
    newest_image_name = get_image_name_with_version(function_info)
    # 查询docker容器是否存在
    try:
        container = client.containers.get(container_name)
        running_image_tags = container.image.tags
        # 判断当前容器的镜像是否为最新版本
        if newest_image_name not in running_image_tags:
            logger.info("container is old version, stop and run new container")
            client.api.remove_container(container_name, force=True)
            container = client.containers.run(newest_image_name, name=container_name, publish_all_ports=True,
                                              detach=True)
            logger.info("new container running,container_id:" + container.id)
        else:
            # 如果没有运行,则运行
            if container.status == 'running':
                logger.info("container is already running,container_id:" + container.id)
            else:
                logger.info("starting container,container_id:" + container.id)
                container.start()
                logger.info("container started,container_id:" + container.id)
    except docker.errors.NotFound:
        logger.info("container not found,run new container")
        container = client.containers.run(newest_image_name, name=container_name, publish_all_ports=True, detach=True)
        logger.info("new container running,container_id:" + container.id)
    # 获取容器映射的端口号
    port = get_run_port(container_name)
    if port != '0':
        proxy_url = 'http://{0}:{1}/proxy/{2}/{3}/{4}'.format(
            configs.mini_fc_host, configs.mini_fc_ops_port, function_info.service_name, function_info.function_name, '')
        return proxy_url
    else:
        return '函数运行失败,请查看日志'


# 获取容器日志
def get_container_log(function_info):
    container_name = get_container_name(function_info)
    return client.api.logs(container_name)


# 获取运行端口
def get_run_port(container_name):
    inspect_result = client.api.inspect_container(container_name)
    try:
        port = inspect_result["NetworkSettings"]["Ports"]["8000/tcp"][0]["HostPort"]
        return port
    except Exception:
        return '0'


# 获取镜像名称
def get_image_name(function_info):
    return "minifc_{0}_{1}".format(function_info.service_name, function_info.function_name)


# 获取带版本的镜像名称
def get_image_name_with_version(function_info):
    return "minifc_{0}_{1}:v{2}".format(function_info.service_name, function_info.function_name,
                                        function_info.function_version)


# 获取容器名称
def get_container_name(function_info):
    return "minifc_{0}_{1}".format(function_info.service_name, function_info.function_name)


def execute_print_cmd(cmd):
    logger.info(cmd)
    os.system(cmd)
