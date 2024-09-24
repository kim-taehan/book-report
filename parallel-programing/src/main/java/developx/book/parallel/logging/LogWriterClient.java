package developx.book.parallel.logging;

import java.io.*;
import java.util.logging.Logger;

public class LogWriterClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Writer writer = new PrintWriter("logwirter.log");
        LogWriter logWriter = new LogWriter(writer);
        logWriter.start();
        logWriter.log("test");
    }
}
