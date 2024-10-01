package developx.book.parallel.cas;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

public class LinkedQueue <E> {

    @RequiredArgsConstructor
    private static class Node <E> {
        final E item;
        final AtomicReference<Node<E>> next;
    }

    private final Node<E> dummy = new Node<>(null, null);
    private final AtomicReference<Node<E>> head = new AtomicReference<>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<>(dummy);

    public boolean put(E item) {
        Node<E> newNode = new Node<>(item, null);
        while (true) {
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next.get();

            if (curTail == tail.get()) {
                if (tailNext != null) {
                    // 큐는 중간 상태이고, 꼬리 이동
                    tail.compareAndSet(curTail, tailNext);
                } else {
                    if (curTail.next.compareAndSet(null, newNode)) {
                        // 추가작업 성공, 꼬리 이동
                        tail.compareAndSet(curTail, newNode);
                        return true;
                    }
                }
            }

        }
    }
}
