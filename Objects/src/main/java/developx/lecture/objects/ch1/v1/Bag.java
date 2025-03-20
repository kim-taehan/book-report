package developx.lecture.objects.ch1.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Bag {
    @Getter
    private Long amount;
    private Invitation invitation;
    @Setter
    private Ticket ticket;

    public Bag(long amount) {
        this(null, amount);
    }

    public Bag(Invitation invitation, long amount) {
        this.invitation = invitation;
        this.amount = amount;
    }

    public boolean hasInvitation() {
        return invitation != null;
    }

    public boolean hasTicket() {
        return ticket != null;
    }

    public void minusAmount(Long amount) {
        this.amount -= amount;
    }
    public void plusAmount(Long amount) {
        this.amount += amount;
    }

}
