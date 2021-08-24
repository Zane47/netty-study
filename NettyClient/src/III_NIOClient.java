import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class III_NIOClient {

    // 定义检测SocketChannel的Selector对象
    private Selector selector = null;
    // 定义处理编码和解码的字符集
    private Charset charset = Charset.forName("UTF-8");
    // 客户端SocketChannel
    private SocketChannel socketChannel = null;

    public void init() throws IOException {
        selector = Selector.open();
        InetSocketAddress inetSocketAddress =
                new InetSocketAddress("127.0.0.1", 30000);
        // 调用open静态方法创建连接到指定主机的SocketChannel
        socketChannel = SocketChannel.open(inetSocketAddress);
        // sc设置成以非阻塞方式工作
        socketChannel.configureBlocking(false);
        // 讲SocketChannel对象注册到指定的Selector
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 启动读取服务器端数据的线程
        new ClientThread().start();

        // 创建键盘输入流
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            // 读取键盘输入
            String line = scan.nextLine();
            // 讲键盘输入的内容输出到SocketChannel中
            socketChannel.write(charset.encode(line));
        }
    }


    // 定义读取服务器数据的线程
    private class ClientThread extends Thread {
        public void run () {
            try {
                while (selector.select() > 0) {
                    // 遍历每个有可用IO操作Channel对应的SelectionKey
                    for (SelectionKey selectionKey : selector.selectedKeys()) {
                        // 删除正在处理的SelectionKey
                        selector.selectedKeys().remove(selectionKey);
                        // 如果该SelectionKey对应的Channel中有可读的数据
                         if (selectionKey.isReadable()) {
                            // 使用NIO读取Channel中的数据
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            String content = "";
                            while (socketChannel.read(byteBuffer) > 0) {
                                socketChannel.read(byteBuffer);
                                byteBuffer.flip();
                                content += charset.decode(byteBuffer);
                            }
                            // 打印输出读取的内容
                            System.out.println("聊天信息：" + content);
                            // 为下一次读取做准备
                            selectionKey.interestOps(SelectionKey.OP_READ);

                        }
                    }



                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new III_NIOClient().init();
    }

}





