import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

public class III_NIOServer {
    // 用于检测所有Channel状态的Selector
    private Selector selector = null;
    // 定义实现编码、解码的字符集对象
    private Charset charset = Charset.forName("UTF-8");

    public void init() throws IOException {
        System.out.println("hello, this is server.");
        selector = Selector.open();
        // 通过open方法打开一个未绑定的ServerSocketChannel实例
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new
                InetSocketAddress("127.0.0.1", 30000);
        // 将该ServerSocketChannel绑定到指定IP地址
        serverSocketChannel.socket().bind(inetSocketAddress);
        // 设置ServerSocket以非阻塞方式工作
        serverSocketChannel.configureBlocking(false);

        // 将serverSocketChannel注册到指定Selector对象
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 服务器上所有Channel都需要向Selector注册，包括ServerSocketChannel和SocketChannel

        // 该selector负责监视这些Socket的IO状态，当其中任意一个或多个具有可用的IO操作时，该Selector的select()方法会返回该Selector上有多少个Channel具有可用的IO操作
        // 并且提供selectedKeys()方法来返回这些Channel对应的SelectionKey集合

        // 正是通过Selector才使得Server端只需要不断调用Selector实例的select()方法，这样就可以知道当前所有Channel是否有需要处理的IO擦欧洲哦
        // 当Selector上注册的所有Channel都没有需要处理的IO操作时，将会阻塞select()方法，此时调用该方法的线程被阻塞

        while (selector.select() > 0) {
            // 依次处理selector上的每个已选择的SelectionKey
            for (SelectionKey selectionKey : selector.selectedKeys()) {
                // 从selector上的已选择Key集中删除正在处理的SelectionKey
                selector.selectedKeys().remove(selectionKey);
                // 如果sk对应的通道包含客户端的链接请求(Client首次连接的时候，isAcceptable = true)
                if (selectionKey.isAcceptable()) {
                    // 调用accept方法接受链接，产生服务器端对应的SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置采用非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 将该SocketChannel也注册到selector
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // 将sk对应的Channel设置成准备接受其他请求
                    selectionKey.interestOps(SelectionKey.OP_ACCEPT);
                }
                // 如果sk对应的通道有数据需要读取 （client发送数据到server时，isReadable = true）
                if (selectionKey.isReadable()) {
                    // 获取该SelectionKey对应的Channel，该Channel中有可读的数据
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 定义准备执行读取数据的ByteBuffer
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    String content = "";
                    // 开始读取数据
                    try {
                        while (socketChannel.read(byteBuffer) > 0) {
                            byteBuffer.flip();
                            content += charset.decode(byteBuffer);
                        }
                        // 打印从该sk对应的Channel里读取到的数据
                        System.out.println("=====" + content);
                        // 将sk对应的Channel设置成准备下一次读取
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    }
                    // 如果捕捉到了该sk对应的Channel出现了异常
                    // 即表明该Channel对应的Client出现了问题，所以从Selector中取消sk的注册
                    catch (IOException e) {
                        e.printStackTrace();
                        // 从Selector中删除指定的SelectionKey
                        selectionKey.cancel();
                        if (null != selectionKey.channel()) {
                            selectionKey.channel().close();
                        }
                    }

                    // 如果content的长度大于0，即聊天信息不为空
                    if (content.length() > 0) {
                        // 遍历该selector里注册的所有SelectKey
                        for (SelectionKey key : selector.keys()) {
                            // 获取该key对应的Channel
                            Channel targetChannel = key.channel();
                            // 如果该Channel是SocketChannel对象
                            if (targetChannel instanceof SocketChannel) {
                                // 讲读取的内容写入到该Channel中
                                SocketChannel dest = (SocketChannel) targetChannel;
                                dest.write(charset.encode(content));
                            }


                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new III_NIOServer().init();
    }
}





