# coding=utf-8
import docker
import docker.errors

import common_util
import configs
import file_tool
from common_util import logger

client = docker.from_env()


# 构建镜像
def build_image(function_info):
    # 创建Dockerfile的目录
    docker_file_dir = file_tool.generate_docker_file(function_info)

    # 构建docker镜像
    image_name = common_util.get_image_name(function_info)

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

    image_name = common_util.get_image_name(function_info)
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
    common_util.execute_print_cmd(cmd)
    return "ok"


# 运行容器
def run_container(function_info):
    container_name = get_container_name(function_info)
    newest_image_name = common_util.get_image_name_with_version(function_info)
    # 查询docker容器是否存在
    try:
        container = client.containers.get(container_name)
        running_image_tags = container.image.tags
        # 判断当前容器的镜像是否为最新版本
        if newest_image_name not in running_image_tags:
            logger.info("container is old version, stop and run new container")
            client.api.remove_container(container_name, force=True)
            container = client.containers.run(
                newest_image_name, name=container_name, publish_all_ports=True, detach=True)
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
        return 'http://{0}:{1}'.format(configs.mini_fc_host, port)
    else:
        return '函数运行失败,请查看日志'


# 获取容器日志
def get_container_log(function_info):
    container_name = get_container_name(function_info)
    return client.api.logs(container_name, tail=20)


# 获取运行端口
def get_run_port(container_name):
    inspect_result = client.api.inspect_container(container_name)
    try:
        port = inspect_result["NetworkSettings"]["Ports"]["8000/tcp"][0]["HostPort"]
        return port
    except Exception:
        return '0'


# 获取容器名称
def get_container_name(function_info):
    return "minifc_{0}_{1}".format(function_info.service_name, function_info.function_name)
