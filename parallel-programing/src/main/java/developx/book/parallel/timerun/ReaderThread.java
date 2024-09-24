package developx.book.parallel.timerun;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ReaderThread extends Thread {

    private final Socket socket;
    private final InputStream in;

    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }

    @Override
    public void interrupt() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        } finally {
            super.interrupt();
        }
    }
    @Override
    public void run() {
        try {
            // 인터럽트에 응답하지 않는 블로킹 작업
            in.read(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
