package william.nio.practise.selector;

/**
 * @author ZhangShenao
 * @date 2023/8/3 5:03 PM
 * @description: NioClient启动类
 */
public class NioClientBootstrap {
    
    public static void main(String[] args) {
        int clientNum = 1;

        for (int i = 0; i < clientNum; i++) {
            new Thread(() -> {
                NioClient client = new NioClient();

                client.connect();
            }).start();
        }
    
    }
}
