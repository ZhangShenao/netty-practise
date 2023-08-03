package william.nio.practise.buffer;


import java.nio.ByteBuffer;

/**
 * @author ZhangShenao
 * @date 2023/8/3 9:52 AM
 * @description: 使用ByteBuffer缓冲
 * <p>ByteBuffer是JDK NIO提供的缓冲,本质上就是一个内存块（byte[]数组）,并在其基础上进行了一系列读、写操作的封装,可以作为内存与磁盘或网卡之间的读写缓冲区</p>
 */
public class ByteBufferDemo {
    
    public static void main(String[] args) {
        //Step1: 初始化ByteBuffer
        ByteBuffer buf = ByteBuffer.allocate(10);
        System.out.println("初始化ByteBuffer:");
        printByteBufferState(buf);  //position=0 limit=10 capacity=10
        
        //Step2: 向ByteBuffer中写入数据
        buf.put("a".getBytes());
        buf.put("b".getBytes());
        buf.put("c".getBytes());
        System.out.println("ByteBuffer中写入3个元素:");
        printByteBufferState(buf);  //position=3 limit=10 capacity=10
        
        //Step3: 将ByteBuffer从写模式切换到读模式
        buf.flip();
        System.out.println("将ByteBuffer从写模式切换到读模式:");
        printByteBufferState(buf);  //position=0 limit=3 capacity=10
        
        //Step4：从ByteBuffer中读取2个元素
        System.out.println("从ByteBuffer中读取2个元素");
        System.out.println((char) buf.get());
        System.out.println((char) buf.get());
        
        //Step5: 查看读取元素后的状态
        System.out.println("读取元素后的状态:");
        printByteBufferState(buf);  //position=2 limit=3 capacity=10
        
    }
    
    private static void printByteBufferState(ByteBuffer buf) {
        System.out.printf("position=%d\tlimit=%d\tcapacity=%d\n", buf.position(), buf.limit(), buf.capacity());
    }
}
