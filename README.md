# 自定义协议

问题:
1、oneway问题,应该由客户端和服务端控制
2、http2 server 不使用多路复用
3、传输层发送信息如果异常与RpcFuture关联起来
4、Rpc异常的状态码值设置
5、http2 setting配置
6、自定义协议存放callerUrl问题
7、继承关系的extension
8、重构URL类
9、优化所有config转url的地方
10、registry,client,server 不与portal绑定，每个portal可提供默认的参数配置和自定义的参数配置
11、服务端传输层异常处理
12、stream通信,一元rpc时的容错处理
13、传输层的客户端修改为池化的客户端

