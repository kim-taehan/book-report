package developx.book.parallel.travel;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.*;

public class TravelCostCalculator {
    private final Set<String> TRAVEL_COMPANY = Set.of("대한항공", "아시아나", "에어제주", "에어부산");
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void getRankedTravelQuotes() throws InterruptedException {

        List<QuoteTask> list = TRAVEL_COMPANY.stream().map(QuoteTask::new).toList();
        List<Future<TravelCost>> futures = executorService.invokeAll(list, 1_000, TimeUnit.MICROSECONDS);

        for (Future<TravelCost> future : futures) {
            TravelCost travelCost = null;
            try {
                travelCost = future.get();
            } catch (ExecutionException | CancellationException e) {
                throw new RuntimeException(e);
            }
            System.out.println(travelCost);
        }
    }

    @RequiredArgsConstructor
    static class QuoteTask implements Callable<TravelCost> {
        private final String travelName;
        @Override
        public TravelCost call() throws Exception {
            return new TravelCost(travelName, new Random().nextInt(10000));
        }
    }
}
