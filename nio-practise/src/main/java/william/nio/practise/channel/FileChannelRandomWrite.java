package william.nio.practise.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ZhangShenao
 * @date 2023/8/3 10:17 AM
 * @description: FileChannel文件随机写
 */
public class FileChannelRandomWrite {
    
    public static void main(String[] args) throws Exception {
        //Step1：创建文件流
        FileOutputStream out = new FileOutputStream(
                "/Users/zhangshenao/Desktop/architect/Netty/netty-practise/nio-practise/src/main/java/william/nio/practise/channel/test.txt");
        
        //Step2: 根据文件流创建FileChannel管道
        FileChannel channel = out.getChannel();
        
        //Step3: 创建Buffer缓冲区,并将数据写入Buffer
        ByteBuffer buffer = ByteBuffer.wrap("a   bc".getBytes());
        
        //Step4: 通过Channel将Buffer中的数据写入文件
        channel.write(buffer);
        
        //Step5: 定位到指定位置,进行文件随机写
        channel.position(1);
        ByteBuffer randomWriteBuffer = ByteBuffer.wrap("666".getBytes());
        channel.write(randomWriteBuffer);
    
        //Step6: 释放资源
        out.close();
        channel.close();
    }
}
