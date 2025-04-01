package developx.lecture.objects.ch1.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Audience {
    private Bag bag;

    public Long buy(Ticket ticket) {
        return this.bag.hold(ticket);
    }
}
