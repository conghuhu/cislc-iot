先不使用滑动窗口协议。

// 关于 父子模块jar包问题
https://blog.csdn.net/java_xxxx/article/details/81181674


分帧与块
默认一块64M，全部接收完以后写出。


约定 ，帧号 从0开始

dtls只是保证数据的正确性？

之前在learn中的版本，client发送数据前需要进行一些处理，发送图片和视频基本上没有丢包。
在这个版本中，client只管发，瞬间发送好多数据包，丢包率非常严重。
// 进程间通信时：
799个包只接收10个左右。
每次发完 延迟1ms 799个包 收到 733个包。而且是本机测试，还没有实现收到确认机制。
// 线程间通信时：
799个包只接收一两个。
每次发完 延迟1ms 799个包 收到 76 个包。而且是本机测试，还没有实现收到确认机制。
每次发完 延迟10ms 799个包 收到 393个包。而且是本机测试，还没有实现收到确认机制。

// 山大校园网通信时：
每次发完延迟1ms 162个包 收到1个包。
每次发完延迟10ms 162个包 收到16个包。
每次发完延迟50ms 162个包 收到86个包。

所以，没有拥塞控制，传输大批数据时，丢包率是个问题。
