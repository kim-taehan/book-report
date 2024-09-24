package developx.book.parallel.executor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutionWebServer {

    private static final int THREAD_COUNT = 100;
    private static final Executor exec = Executors.newFixedThreadPool(THREAD_COUNT);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(80);
        while (true) {
            Socket accept = serverSocket.accept();
            Runnable runnable = () -> handleRequest(accept);
            exec.execute(runnable);
        }
    }

    private static void handleRequest(Socket accept) {
        //.. do something
    }
}
