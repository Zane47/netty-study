import java.net.*;
import java.io.*;


public class II_Client2WithThread {


    public static void main(String[] args) throws IOException {
        System.out.println("hello, here is II_Client2WithThread");

        Socket socket = new Socket("127.0.0.1", 30000);
        // client其中ClientThread线程不断读取来自服务器的数据
        new Thread(new II_ClientThreadDo(socket)).start();

        // 获取该Socket对应的输出流
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        String line = null;
        // 不断从键盘读取输入
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(System.in)
        );
        while (null != (line = bufferedReader.readLine())) {
            // 将键盘的输入内容写入Socket对应的输出流
            printStream.println(line);
        }
    }
}
