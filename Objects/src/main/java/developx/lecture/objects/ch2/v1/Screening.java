package developx.lecture.objects.ch2.v1;

import java.time.LocalDateTime;

public class Screening {

    private Movie movie;
    private int sequence;

    private LocalDateTime localDateTime;

    public Screening(Movie movie, int sequence, LocalDateTime localDateTime) {
        this.movie = movie;
        this.sequence = sequence;
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getStartTime() {
        return this.localDateTime;
    }

    public boolean isSequence(int sequence) {
        return this.sequence == sequence;
    }

    public Money getMovieFee() {
        return movie.getFee();
    }

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }
}
