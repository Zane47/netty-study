package nio.FileChannel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *使用前面学习后的ByteBuffer(缓冲) 和 FileChannel(通道)，
 * 将 "hello world" 写入到file01.txt
 * 文件不存在就创建
 */

public class NIOFileChannel01WriteFile {

    public static void main(String[] args) throws IOException {
        String str = "hello world";

        //创建一个输出流->channel

        FileOutputStream fileOutputStream = new FileOutputStream("src/NIO/FileChannel/file01.txt");


        //通过 fileOutputStream 获取 对应的 FileChannel
        //这个 fileChannel 真实 类型是  FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将 str 放入 byteBuffer
        byteBuffer.put(str.getBytes());


        // 对byteBuffer 进行flip
        // 此时position是在最后，需要做一个反转，然后再写入Channel
        byteBuffer.flip();

        //将byteBuffer 数据写入到 fileChannel
        fileChannel.write(byteBuffer);

        fileOutputStream.close();
    }

}
