FROM daocloud.io/python:2.7
MAINTAINER "hzlvzimin <hzlvzimin@corp.netease.com>"
COPY requirements.txt /usr/local/requirements.txt
RUN pip install --upgrade pip && pip install -r /usr/local/requirements.txt
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
COPY ${MODULE_NAME}.py /usr/local/${MODULE_NAME}.py
COPY server.py.tmpl /usr/local/server_${RANDOM}.py
EXPOSE 8000
CMD ["python","/usr/local/server_${RANDOM}.py"]