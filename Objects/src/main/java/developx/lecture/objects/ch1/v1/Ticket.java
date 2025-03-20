package developx.lecture.objects.ch1.v1;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Ticket {
    private Long fee;

    public Ticket(Long fee) {
        this.fee = fee;
    }
}
