FROM daocloud.io/python:2.7
MAINTAINER "g4seek <g4seek@gmail.com>"
COPY requirements.txt /usr/local/requirements.txt
RUN pip install --upgrade pip && pip install -r /usr/local/requirements.txt
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
COPY server.py.tmpl /usr/local/server_${RANDOM}.py
COPY ${RANDOM}/* /usr/local/
EXPOSE 8000
CMD ["python","/usr/local/server_${RANDOM}.py"]