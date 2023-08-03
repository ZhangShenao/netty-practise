package william.nio.practise.selector;

/**
 * @author ZhangShenao
 * @date 2023/8/3 5:03 PM
 * @description: NioServer启动类
 */
public class NioServerBootstrap {
    
    public static void main(String[] args) {
        NioServer server = new NioServer();
        
        server.init();
        
        server.listen();
    }
}
