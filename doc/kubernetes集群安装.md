### 使用kubeadm安装master节点
0. 基本信息
```
kubeadm版本:1.13.1
master ip : 10.216.40.206 host : kube-node1
node ip : 10.216.40.96 host : kube-node2
```
1. 安装docker
```
yum install docker
```

2. 修改docker镜像仓库
```
tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://j131h26n.mirror.aliyuncs.com"]
}
EOF
```

3. 修改docker cgroup配置
```
vim /usr/lib/systemd/system/docker.service

--exec-opt native.cgroupdriver=systemd
改为
--exec-opt native.cgroupdriver=cgroupfs
```

4. 启动docker
```
systemctl daemon-reload
systemctl start docker
```

5. 配置yum镜像源
```
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
EOF
yum -y install epel-release
yum clean all
yum makecache
```

6. 通过yum安装k8s软件包
```
yum install kubeadm.x86_64 kubectl.x86_64 kubelet.x86_64 kubernetes-cni.x86_64 cri-tools.x86_64 -y
```

7. 手动下载k8s镜像(可选)
```
docker pull k8s.gcr.io/kube-proxy-amd64:v1.10.0 &
docker pull k8s.gcr.io/kube-scheduler-amd64:v1.10.0 &
docker pull k8s.gcr.io/kube-controller-manager-amd64:v1.10.0 &
docker pull k8s.gcr.io/kube-apiserver-amd64:v1.10.0 &
docker pull k8s.gcr.io/echoserver:1.10 &
docker pull k8s.gcr.io/etcd-amd64:3.1.12 &
docker pull k8s.gcr.io/kube-addon-manager:v8.6 &
docker pull k8s.gcr.io/k8s-dns-dnsmasq-nanny-amd64:1.14.8 &
docker pull k8s.gcr.io/k8s-dns-sidecar-amd64:1.14.8 &
docker pull k8s.gcr.io/k8s-dns-kube-dns-amd64:1.14.8 &
docker pull k8s.gcr.io/pause-amd64:3.1 &
docker pull k8s.gcr.io/kubernetes-dashboard-amd64:v1.8.1 &
```

8. 启动kubelet
```
systemctl daemon-reload
systemctl start kubelet
```
此时kubelet状态为exited

9. 执行kubeadm命令生成配置文件,启动kube节点
```
kubeadm reset
kubeadm init --apiserver-advertise-address 10.216.40.206
```
记录执行结果中的kube join命令
```
kubeadm join 10.216.40.206:6443 --token udnwo5.08nzvtjx59b3b3xs --discovery-token-ca-cert-hash sha256:01fcdc7ca38ac85ebe91655d29f10805fae61a2c27718c01a78ff688f19185a5
```

10. 修改kubelet配置

`/var/lib/kubelet/kubeadm-flags.env` 去掉cni插件的配置
```
KUBELET_KUBEADM_ARGS=--cgroup-driver=systemd --pod-infra-container-image=k8s.gcr.io/pause:3.1
```

11. 重启kubelet
```
systemctl daemon-reload
systemctl restart kubelet
```

12. 修改kubectl的配置文件  

`~/.kube/config`
```yaml
apiVersion: v1
clusters:
- cluster:
    certificate-authority: /etc/kubernetes/pki/ca.crt
    server: https://10.216.40.206:6443
  name: my-cluster
contexts:
- context:
    cluster: my-cluster
    user: root
  name: my-cluster
current-context: my-cluster
kind: Config
preferences: {}
users:
- name: root
  user:
    client-certificate: /etc/kubernetes/pki/apiserver-kubelet-client.crt
    client-key: /etc/kubernetes/pki/apiserver-kubelet-client.key
```

13. 设置master节点可参与调度
```
kubectl taint nodes --all node-role.kubernetes.io/master-
```

#### 使用kubeadm安装node节点
1. 执行安装master节点的1-8步骤

2. 重置kubeadm,并加入集群
```
kubeadm reset
kubeadm join 10.216.40.206:6443 --token udnwo5.08nzvtjx59b3b3xs --discovery-token-ca-cert-hash sha256:01fcdc7ca38ac85ebe91655d29f10805fae61a2c27718c01a78ff688f19185a5
```

3. 


#### 问题解决
1. 9-11步可能需要反复尝试多次
```
通过systemctl status kubelet -l查看日志
在github上查找对应的issue进行调整
可以优先看点赞多的回复
```
2. 运行时提示Nameserver limits were exceeded
```
在/etc/resolv.conf中删除多余的配置项
```

#### 参考资料
- https://kubernetes.io/docs/setup/independent/create-cluster-kubeadm/
- https://github.com/opsnull/follow-me-install-kubernetes-cluster  
- https://www.kubernetes.org.cn/4291.html  
- https://www.kubernetes.org.cn/3805.html  
- https://juejin.im/post/5b45d4185188251ac062f27c
- https://jimmysong.io/posts/kubernetes-installation-on-centos/
