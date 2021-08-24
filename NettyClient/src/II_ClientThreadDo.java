import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class II_ClientThreadDo implements Runnable {
    // 该线程负责处理的Socket
    private Socket socket;
    // 该线程所处理的Socket所对应的输入流
    BufferedReader bufferedReader = null;

    public II_ClientThreadDo(Socket socket) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
    }

    @Override
    public void run() {
        try {
            String content = null;
            // 不断读取Socket输入流中的内容，并将这些内容打印输出
            while (null != (content = bufferedReader.readLine())) {
                System.out.println("ClientThreadDo: " + content);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }




}
