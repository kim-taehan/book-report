package developx.book.effective.ch3.item14;

import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@RequiredArgsConstructor
public class ExceptionAdvice implements Comparable<ExceptionAdvice> {

    private final int order;
    @Override
    public int compareTo(ExceptionAdvice ea) {
//        return Integer.compare(this.order, ea.order);
        return comparator.compare(this, ea);
    }

    static Comparator<ExceptionAdvice> comparator = Comparator.comparingInt(value -> value.order);

    @Override
    public String toString() {
        return "ExceptionAdvice{" +
                "order=" + order +
                '}';
    }
}
