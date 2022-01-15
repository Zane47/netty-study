import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class II_ServerWithThread {
    // 定义保存所有Socket的ArrayList
    public static ArrayList<Socket> socketArrayList = new ArrayList<Socket>();


    public static void main(String[] args) throws IOException {
        System.out.println("hello, here is II_ServerWithThread");
        ServerSocket serverSocket = new ServerSocket(30000);
        while (true) {
            // 一直阻塞，直到别人连接
            Socket socket = serverSocket.accept();
            socketArrayList.add(socket);

            // 每当client连接后启动一条线程（ServerThread）为该client服务
            new Thread(new II_ServerThreadDo(socket)).start();


            PrintStream printStream = new PrintStream(socket.getOutputStream());
            String line = null;
            // 不断从键盘读取输入
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            while (null != (line = bufferedReader.readLine())) {
                // 将键盘的输入内容写入Socket对应的输出流
                printStream.println("From Server: " + line);
            }
        }
    }
}
