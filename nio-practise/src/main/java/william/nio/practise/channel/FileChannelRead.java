package william.nio.practise.channel;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ZhangShenao
 * @date 2023/8/3 10:35 AM
 * @description: FileChannel读文件
 */
public class FileChannelRead {
    
    public static void main(String[] args) throws Exception {
        //Step1：创建文件流
        FileInputStream in = new FileInputStream(
                "/Users/zhangshenao/Desktop/architect/Netty/netty-practise/nio-practise/src/main/java/william/nio/practise/channel/test.txt");
        
        //Step2: 根据文件流创建FileChannel管道
        FileChannel channel = in.getChannel();
        
        //Step3: 创建Buffer,作为数据读取缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(30);
        
        //Step4: 使用FileChannel将磁盘文件读取至Buffer
        channel.read(buffer);
        
        //Step5: 处理Buffer中读取到的数据
        buffer.flip();  //将Buffer从写模式切换至读模式
        char[] content = new char[buffer.limit()];
        for (int i = 0; i < buffer.limit(); i++) {
            content[i] = (char) buffer.get(i);
        }
        System.out.println("File Content: " + new String(content));
        
        //Step6: 释放资源
        in.close();
        channel.close();
    }
}
