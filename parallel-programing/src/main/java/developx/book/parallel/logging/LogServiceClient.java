package developx.book.parallel.logging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;

public class LogServiceClient {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        PrintWriter writer = new PrintWriter("logService.log");
        LogService logService = new LogService(writer);

        logService.log("한글 로그1");
        logService.log("한글 로그2");

        logService.stop();
        logService.log("남겨지지 않는 로그");
    }
}
