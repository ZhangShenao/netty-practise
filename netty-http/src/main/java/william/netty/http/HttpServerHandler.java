package william.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.StandardCharsets;

/**
 * @author ZhangShenao
 * @date 2023/8/4 11:14 AM
 * @description: Http服务处理器
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    
    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";
    
    private static final String RESPONSE_BODY = "<html>\n" + "<body>Welcome to netty http server~</body>\n" + "</html>";
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        HttpMethod method = msg.method();
        String uri = msg.uri();
        System.out.println("receive http request. method: " + method + ", uri: " + uri);
        
        //创建响应
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        
        //构造response header
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);
        
        //构造response body
        ByteBuf body = Unpooled.copiedBuffer(RESPONSE_BODY, StandardCharsets.UTF_8);
        
        //将body写入response
        response.content().writeBytes(body);
        
        //释放对ByteBuf的引用
        body.release();
        
        //将response相应给客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
