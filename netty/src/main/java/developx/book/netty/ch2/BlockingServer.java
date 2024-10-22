package developx.book.netty.ch2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingServer {

    public static void main(String[] args) {

        Thread thread = new Thread(new BlockingServerRunnable());
        thread.start();

    }

    private static class BlockingServerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(8888);

                while (true) {
                    Socket socket = serverSocket.accept();
                    OutputStream outputStream = socket.getOutputStream();
                    InputStream inputStream = socket.getInputStream();

                    while (true) {
                        try {
                            int request = inputStream.read();
                            outputStream.write(request);
                        } catch (IOException e) {
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
