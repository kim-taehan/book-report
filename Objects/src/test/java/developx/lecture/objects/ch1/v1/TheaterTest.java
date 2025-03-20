package developx.lecture.objects.ch1.v1;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@DisplayName("v1 테스트")
class TheaterTest {

    @DisplayName("초대장이 없는 관람객은 현금으로 구매할 수 있다. ")
    @Test
    void enter(){
        // given
        TicketOffice ticketOffice = new TicketOffice(1000L, new Ticket(2000L), new Ticket(2000L));
        TicketSeller ticketSeller = new TicketSeller(ticketOffice);
        Theater theater = new Theater(ticketSeller);

        // when
        Bag bag = new Bag(10000L);
        theater.enter(new Audience(bag));

        // then
        Assertions.assertThat(ticketOffice.getAmount()).isEqualTo(3000L);
        Assertions.assertThat(bag.getAmount()).isEqualTo(8000L);
    }


    @DisplayName("초대장이 있는 관람객은 초대장으로 구매할 수 있다. ")
    @Test
    void enterByInvitation(){
        // given
        TicketOffice ticketOffice = new TicketOffice(1000L, new Ticket(2000L), new Ticket(2000L));
        TicketSeller ticketSeller = new TicketSeller(ticketOffice);
        Theater theater = new Theater(ticketSeller);

        // when
        Bag bag = new Bag(new Invitation(), 10000L);
        theater.enter(new Audience(bag));

        // then
        Assertions.assertThat(ticketOffice.getAmount()).isEqualTo(1000L);
        Assertions.assertThat(bag.getAmount()).isEqualTo(10000L);
    }


    @Test
    void lockTest() throws ExecutionException, InterruptedException {

        long startTime = System.currentTimeMillis(); // 시작 시간


        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(10);
        Addition addition = new Addition();

        List<Future<?>> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Future<?> submit = cachedThreadPool.submit(() -> {
                addition.addSync();
                addition.hello(startTime);
            });
            list.add(submit);
        }

        for (Future<?> future : list) {
            future.get();
        }

    }


    BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1000);
    @Test
    void singleThread() throws ExecutionException, InterruptedException {

        long startTime = System.currentTimeMillis(); // 시작 시간
        Addition addition = new Addition();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 1000; i++) {
            executorService.execute(()->{
                addition.addAnSync();
                blockingQueue.add("");
            });
        }
        List<Future<?>> list = new ArrayList<>();


        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            cachedThreadPool.execute(() -> {
                while (!blockingQueue.isEmpty()) {
                    try {
                        String take = blockingQueue.take();
                        addition.hello(startTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        Thread.sleep(1000);
    }

    static class Addition {
        private int index = 0;

        public synchronized void addSync(){

            for (int i = 0; i < 100_000_000; i++) {

            }

            index++;
        }
        public void addAnSync(){


            index++;
        }
        public void hello(long startTime){


            System.out.println("소요시간 = " + (System.currentTimeMillis() - startTime) + " : " + index);
        }
    }
}