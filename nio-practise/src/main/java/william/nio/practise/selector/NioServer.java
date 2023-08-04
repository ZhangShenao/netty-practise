package william.nio.practise.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author ZhangShenao
 * @date 2023/8/3 4:03 PM
 * @description: 基于Selector模式的NIO服务端
 */
public class NioServer {
    
    /**
     * 服务端主机号
     */
    public static final String SERVER_HOST = "localhost";
    
    /**
     * 服务端监听端口号
     */
    public static final int SERVER_PORT = 8888;
    
    /**
     * 默认缓冲区大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    
    /**
     * TCP Back Log 队列大小
     */
    private static final int BACK_LOG_SIZE = 200;
    
    /**
     * 服务端的Selector多路复用器
     */
    private Selector selector;
    
    
    /**
     * 服务端初始化
     */
    public void init() {
        try {
            //创建ServerSocketChannel通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            
            //将通道设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            
            //监听端口
            serverSocketChannel.socket().bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT), BACK_LOG_SIZE);
            
            //开启Selector
            selector = Selector.open();
            
            //将ServerSocketChannel注册到Selector上,并注册Accept网络连接事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            System.out.println("server initialized!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 服务端启动并开始监听
     */
    public void listen() {
        //轮询Selector
        while (true) {
            try {
                //从Selector上查看是否有网络事件触发。该操作是阻塞的
                selector.select();
                
                //获取已触发的网络事件
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                
                //遍历网络事件,进行相应处理
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();  //删除已处理的网络事件,避免重复
                    
                    if (selectionKey.isAcceptable()) {   //处理网络连接事件
                        accept(selectionKey);
                    } else if (selectionKey.isReadable()) {   //处理网络读事件
                        read(selectionKey);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * 处理网络事件
     */
    private void accept(SelectionKey selectionKey) {
        System.out.println("server handle accept event");
        
        //通过TCP三次握手,与客户端建立连接,获取用于通信的SocketChannel
        try (ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel()) {
            SocketChannel channel = serverSocketChannel.accept();
            
            channel.configureBlocking(false); //将通信Channel设置为非阻塞
            
            //注册网络读事件
            channel.register(selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 处理网络读事件
     */
    private void read(SelectionKey selectionKey) {
        System.out.println("server handle read event");
        
        //获取网络通信的Channel
        try (SocketChannel channel = (SocketChannel) selectionKey.channel()) {
            //分配读读缓冲区
            ByteBuffer readBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
            
            //将Channel中的数据读入缓冲区
            int readCount = channel.read(readBuffer);
            
            //解析请求数据
            if (readCount > 0) {
                readBuffer.flip();  //将ReadBuffer切换为读模式
                String request = StandardCharsets.UTF_8.decode(readBuffer).toString();
                System.out.println(
                        "server receive request. msg: " + request + ", address: " + channel.getRemoteAddress());
                
                //构造响应数据,并通过Channel发送到客户端
                String resp = "server response";
                channel.write(ByteBuffer.wrap(resp.getBytes(StandardCharsets.UTF_8)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
