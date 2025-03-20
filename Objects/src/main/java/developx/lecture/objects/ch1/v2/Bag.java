package developx.lecture.objects.ch1.v2;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Bag {
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


    public Long hold(Ticket ticket) {
        if (this.hasInvitation()) {
            this.setTicket(ticket);
            return 0L;
        } else {
            this.minusAmount(ticket.getFee());
            this.setTicket(ticket);
            return ticket.getFee();
        }
    }
}
