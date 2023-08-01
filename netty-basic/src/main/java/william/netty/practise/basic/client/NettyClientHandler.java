package william.netty.practise.basic.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author ZhangShenao
 * @date 2023/8/1 2:15 PM
 * @description: Netty客户端网络事件处理器
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    
    //网络通道激活事件回调
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //在Channel数据通道激活后,向服务器发送请求
        String req = "netty client request";
        byte[] reqData = req.getBytes(StandardCharsets.UTF_8);
        ByteBuf reqBuffer = Unpooled.buffer(reqData.length);
        reqBuffer.writeBytes(reqData);
        ctx.writeAndFlush(reqBuffer);
    }
    
    //读取数据事件回调
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //读取服务器响应
        ByteBuf respBuffer = (ByteBuf) msg;
        byte[] respData = new byte[respBuffer.readableBytes()];
        respBuffer.readBytes(respData);
        String resp = new String(respData, StandardCharsets.UTF_8);
        System.out.println("receive response from server: " + resp);
    }
    
    //异常捕获事件回调
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("exception caught in netty client!");
        cause.printStackTrace();
        ctx.close();
    }
}
