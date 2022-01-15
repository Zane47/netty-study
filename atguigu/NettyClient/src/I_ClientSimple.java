import java.net.*;
import java.io.*;

public class I_ClientSimple {
    public static void main(String[] args) throws IOException {
        System.out.println("here is Client");

        Socket socket = new Socket("127.0.0.1", 30000);
        // 执行完这句的时候，Server则从阻塞状态接着往下
        // Server的阻塞: serverSocket.accept();

        // 将Socket对应的输入流包装成BufferedReader
        BufferedReader br = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        // 进行普通的IO操作
        // 如果Server未执行: ps.println("happy new year!");该句会阻塞
        String line = br.readLine();
        System.out.println("from Server: " + line);
        // 关闭输入流和Socket
        br.close();
        socket.close();
        System.out.println("Client: close");
    }
}
