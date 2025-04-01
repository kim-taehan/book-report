package developx.lecture.objects.ch2.v1;

import developx.lecture.objects.ch2.v1.discount.policy.DisCountPolicy;

import java.time.Duration;

public class Movie {

    private String title;
    private Duration runningTime;
    private Money fee;
    private DisCountPolicy disCountPolicy;


    public Movie(String title, Duration runningTime, Money fee, DisCountPolicy disCountPolicy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.disCountPolicy = disCountPolicy;
    }

    public Money getFee() {
        return this.fee;
    }

    public Money calculateMovieFee(Screening screening) {
        return this.fee.minus(disCountPolicy.calculateDiscountAmount(screening));
    }
}
