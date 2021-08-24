import java.net.*;
import java.io.*;


// Thread1:
// 读取用户的键盘输入，并将用户输入的数据写入Socket对应的输出流中
// MyClient(主线程)负责

// Thread2:
// 读取Socket对应输入流中的数据（从服务器发送过来的数据）
// 并将这些数据打印输出


public class II_ClientWithThread {
    public static void main(String[] args) throws IOException {
        System.out.println("hello, here is II_ClientWithThread");

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
