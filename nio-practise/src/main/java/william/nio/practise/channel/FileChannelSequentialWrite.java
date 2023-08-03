package william.nio.practise.channel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ZhangShenao
 * @date 2023/8/3 10:17 AM
 * @description: FileChannel文件顺序写
 * <p>在JDK NIO中，使用Buffer+Channel配合的方式来实现I/O操作。Buffer作为数据的缓存，而Channel是实际数据读写的通道。</p>
 * <p>在读取时，先通过Channel将磁盘或网卡中的数据读取至Buffer，再由用户程序对Buffer进行处理</p>
 * <p>在写入时，用户程序先将数据写Buffer，再由Channel读取Buffer中的数据并写入磁盘或网卡</p>
 */
public class FileChannelSequentialWrite {
    
    public static void main(String[] args) throws Exception {
        //Step1：创建文件流
        FileOutputStream out = new FileOutputStream(
                "/Users/zhangshenao/Desktop/architect/Netty/netty-practise/nio-practise/src/main/java/william/nio/practise/channel/test.txt");
        
        //Step2: 根据文件流创建FileChannel管道
        FileChannel channel = out.getChannel();
        
        //Step3: 创建Buffer缓冲区,并将数据写入Buffer
        ByteBuffer buffer = ByteBuffer.wrap("abc".getBytes());
        
        //Step4: 通过Channel将Buffer中的数据写入文件
        channel.write(buffer);
        channel.force(true);    //强制刷盘：FileChannel会将数据写入操作系统的PageCache，并不会立即写入磁盘。force方法可以强制刷盘
        
        //Step5: 查看当前Buffer的状态
        System.out.printf("position=%d\tlimit=%d\tcapacity=%d\n", buffer.position(), buffer.limit(), buffer.capacity());
        
        //Step6: 释放资源
        out.close();
        channel.close();
    }
}
