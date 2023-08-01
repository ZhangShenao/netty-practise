package william.netty.practise.basic.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ZhangShenao
 * @date 2023/8/1 1:39 PM
 * @description: 使用Netty实现的网络通信服务端
 * <p>主要用于Netty核心组件的初始化,以及服务端端口绑定</p>
 */
public class NettyServer {
    
    public static final int PORT = 8001;
    
    public static void main(String[] args) {
        //Step1: 基于Reactor模式,分别创建两个用于处理网络请求的线程组——EventLoopGroup
        EventLoopGroup parentGroup = new NioEventLoopGroup();  //Acceptor线程组
        EventLoopGroup childGroup = new NioEventLoopGroup();    //Processor/Handler线程组
        
        try {
            //Step2: 初始化服务器启动类ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();    //ServerBootstrap相当于Netty服务器
            
            //Step3: 对服务器进行一系列配置
            serverBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)  //监听NioServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG, 1024)  //设置TCP缓冲队列大小
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new NettyServerHandler());   //注册自定义的网络事件处理器
                        }
                    });
            
            System.out.println("netty server started on port: " + PORT);
            
            //Step4: 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            
            //Step5: 等待服务器关闭
            channelFuture.channel().closeFuture().sync();   //同步等待服务器关闭的结果
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //Step6: 服务器关闭,优雅停机
            System.out.println("netty server stopped.");
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
