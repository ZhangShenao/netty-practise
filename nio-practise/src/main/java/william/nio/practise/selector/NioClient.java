package william.nio.practise.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ZhangShenao
 * @date 2023/8/3 4:39 PM
 * @description: 基于Selector模式的Nio客户端
 */
public class NioClient {
    
    
    /**
     * 连接远程服务器
     */
    public void connect() {
        SocketChannel socketChannel = null;
        Selector selector = null;
        try {
            //创建SocketChannel通道
            socketChannel = SocketChannel.open();
            
            //将通道设置为非阻塞
            socketChannel.configureBlocking(false);
            
            //连接远程服务器
            socketChannel.connect(new InetSocketAddress(NioServer.SERVER_HOST, NioServer.SERVER_PORT));
            
            //开启Selector多路复用器
            selector = Selector.open();
            
            //向注册网络连接事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            
            //轮询Selector
            while (true) {
                //从Selector上查看是否有网络事件触发。该操作是阻塞的
                selector.select();
                
                //获取已触发的网络事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                
                //遍历网络事件,进行相应处理
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();  //删除已处理的网络事件,避免重复
                    
                    if (selectionKey.isConnectable()) { //处理网络连接事件
                        if (socketChannel.finishConnect()) { //连接已就绪,向服务端发送请求
                            //注册网络读事件
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //                            selectionKey.interestOps(SelectionKey.OP_READ);
                            
                            //向服务端发送请求
                            String req = "client request";
                            socketChannel.write(ByteBuffer.wrap(req.getBytes(StandardCharsets.UTF_8)));
                        } else { //连接未就绪,取消此次事件
                            selectionKey.cancel();
                        }
                    } else if (selectionKey.isReadable()) {   //处理网络读事件,接收服务端的响应
                        //将响应数据读入缓冲区
                        ByteBuffer buf = ByteBuffer.allocate(NioServer.DEFAULT_BUFFER_SIZE);
                        socketChannel.read(buf);
                        buf.flip(); //将ByteBuffer切换为读模式
                        
                        //解析响应
                        String response = StandardCharsets.UTF_8.decode(buf).toString();
                        System.out.println("receive response from server. response: " + response + ", thread: "
                                + Thread.currentThread().getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //释放资源
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    
}
