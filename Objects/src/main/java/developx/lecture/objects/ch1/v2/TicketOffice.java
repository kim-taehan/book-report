package developx.lecture.objects.ch1.v2;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicketOffice {

    @Getter
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketOffice(Long amount, Ticket... tickets) {
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }

    public Ticket getTicket() {
        return tickets.removeFirst();
    }

    public void minusAmount(Long amount) {
        this.amount -= amount;
    }
    public void plusAmount(Long amount) {
        this.amount += amount;
    }

}
