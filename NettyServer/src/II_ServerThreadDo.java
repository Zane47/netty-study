import java.io.*;
import java.net.*;

public class II_ServerThreadDo implements Runnable {
    // 定义当前线程所处理的Socket
    Socket socket = null;
    // 该线程所处理的Socket所对应的输入流
    BufferedReader bufferedReader = null;

    public II_ServerThreadDo(Socket socket) throws IOException {
        this.socket = socket;
        // 初始化该Socket对应的输入流
        bufferedReader = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
    }


    @Override
    public void run() {
        try {
            String content = null;
            // 循环，不断从Socket中读取Client发过来的数据
            while (null != (content = readFromClient())) {
                // 将读到的内容向每个Socket发送一次
                for (Socket socket : II_ServerWithThread.socketArrayList) {
                    PrintStream printStream = new PrintStream(socket.getOutputStream());
                    // printStream.println("ServerThreadDo: " + content);
                    // printStream.println("From Server: I get your message");
                    System.out.println("ServerThreadDo: From Client: " + content);
                }
            }



        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private String readFromClient() {
        try{
            return bufferedReader.readLine();
        }
        // 如果捕捉到异常，说明该socket对应的客户端已经关闭
        catch (IOException e) {
            // 删除该Socket
            II_ServerWithThread.socketArrayList.remove(socket);
        }

        return null;
    }

}
