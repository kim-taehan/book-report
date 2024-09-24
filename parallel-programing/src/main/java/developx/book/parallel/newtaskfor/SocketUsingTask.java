package developx.book.parallel.newtaskfor;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public abstract class SocketUsingTask<T> implements CancellableTask<T> {

    private Socket socket;

    public synchronized void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void cancel() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {

        }
    }

    @Override
    public RunnableFuture<T> newTask() {
        return new FutureTask<T>(this){
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {

                try{
                    SocketUsingTask.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }
}
