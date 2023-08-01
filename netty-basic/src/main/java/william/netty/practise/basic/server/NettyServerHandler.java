package william.netty.practise.basic.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author ZhangShenao
 * @date 2023/8/1 1:53 PM
 * @description: Netty服务端网络事件处理器
 * <p>角色对应为Reactor模式中的Processor线程,用于读取网络请求并发送响应</p>
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    
    //读取数据事件回调
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //Step1: 读取客户端请求
        ByteBuf reqBuffer = (ByteBuf) msg;
        byte[] data = new byte[reqBuffer.readableBytes()];
        reqBuffer.readBytes(data);
        String req = new String(data, StandardCharsets.UTF_8);
        System.out.println("receive request from client: " + req);
        
        //Step2: 向客户端发送响应
        String resp = "netty server response";
        ByteBuf respBuffer = Unpooled.copiedBuffer(resp.getBytes(StandardCharsets.UTF_8));
        ctx.write(respBuffer);  //此时只是把数据写入到Channel,并不一定会立即发送到客户端,操作系统会根据自己的实际情况执行数据发送
    }
    
    //读取数据完成事件回调
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //执行真正的数据发送
        ctx.flush();
    }
    
    //异常捕获事件回调
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("exception caught in netty server!");
        cause.printStackTrace();
        ctx.close();
    }
    
    //网络通道激活事件回调
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("netty server channel is active");
    }
}
