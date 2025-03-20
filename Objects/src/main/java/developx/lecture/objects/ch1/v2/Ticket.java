package developx.lecture.objects.ch1.v2;

import lombok.Getter;

@Getter
public class Ticket {
    private Long fee;

    public Ticket(Long fee) {
        this.fee = fee;
    }
}
