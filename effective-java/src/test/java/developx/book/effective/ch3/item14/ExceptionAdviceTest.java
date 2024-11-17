package developx.book.effective.ch3.item14;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionAdviceTest {

    @Test
    void compareTo() {


        List<ExceptionAdvice> list = new ArrayList<>();

        list.add(new ExceptionAdvice(1));
        list.add(new ExceptionAdvice(10));
        list.add(new ExceptionAdvice(11));
        list.add(new ExceptionAdvice(8));
        list.add(new ExceptionAdvice(9));
        list.add(new ExceptionAdvice(2));



        List<ExceptionAdvice> result = list.stream().sorted().toList();

        for (ExceptionAdvice exceptionAdvice : result) {
            System.out.println("exceptionAdvice = " + exceptionAdvice);
        }

    }
}