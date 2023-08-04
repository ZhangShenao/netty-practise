package william.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author ZhangShenao
 * @date 2023/8/3 6:36 PM
 * @description: Netty Http服务端
 */
public class NettyHttpServer {
    
    private static final int PORT = 7001;
    
    private static final int TCP_BACKLOG_SIZE = 1024;
    
    private static final int HTTP_MAX_CONTENT_LENGTH = 65535;
    
    public static void main(String[] args) {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, TCP_BACKLOG_SIZE)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            //添加一系列HTTP协议相关的处理器
                            ch.pipeline().addLast("http-request-decoder", new HttpRequestDecoder())  //HTTP请求解码器
                                    .addLast("http-object-aggregator",
                                            new HttpObjectAggregator(HTTP_MAX_CONTENT_LENGTH))    //HTTP对象聚合器
                                    .addLast("http-response-encoder", new HttpResponseEncoder())  //HTTP响应编码器
                                    .addLast("chunked-write-handler",
                                            new ChunkedWriteHandler()) //chunked传输处理器：使用Transfer-Encoding: chunked代替Content-Length
                                    .addLast("http-server-handler", new HttpServerHandler())    //自定义请求处理器
                                    .addLast(new HttpServerHandler());
                        }
                    });
            
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            System.out.println("http server started on port: " + PORT);
            
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }
    }
}
