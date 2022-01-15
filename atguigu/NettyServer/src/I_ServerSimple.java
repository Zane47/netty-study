import java.net.*;
import java.io.*;

public class I_ServerSimple {
    public static void main(String[] args) throws IOException {
        System.out.println("here is Server");

        // create a ServerSocket to listen the connection request from client's socket
        ServerSocket serverSocket = new ServerSocket(30000);

        // 采用循环不断接受来自客户端的请求
        while (true) {
            System.out.println("Server: in while start");
            // 每当接受到客户端Socket的请求，服务器端也对应产生一个Socket
            Socket s = serverSocket.accept();
            // 将socket对应的输出流包装成PrintStream
            PrintStream ps = new PrintStream(s.getOutputStream());
            // 进行普通的IO操作
            ps.println("happy new year!");
            // 关闭输出流，关闭Socket
            ps.close();
            s.close();
            System.out.println("Server: close");

        }
    }
}
