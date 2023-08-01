package william.netty.practise.basic.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import william.netty.practise.basic.server.NettyServer;

/**
 * @author ZhangShenao
 * @date 2023/8/1 2:05 PM
 * @description: 使用Netty实现的网络通信客户端
 */
public class NettyClient {
    
    public static void main(String[] args) {
        //Step1: 创建用于处理网络请求的线程组
        EventLoopGroup parentGroup = new NioEventLoopGroup();   //客户端处理的网络事件较少,只需要一个Acceptor线程组即可
        
        try {
            //Step2: 初始化客户端启动类Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            
            //Step3: 对客户端进行一系列配置
            bootstrap.group(parentGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new NettyClientHandler());    //注册自定义的网络事件处理器
                        }
                    });
            
            //Step4: 向服务器发起连接
            ChannelFuture channelFuture = bootstrap.connect("localhost", NettyServer.PORT).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
