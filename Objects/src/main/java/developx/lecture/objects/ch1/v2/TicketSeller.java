package developx.lecture.objects.ch1.v2;

import lombok.Getter;

public class TicketSeller {

    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        Ticket ticket = this.ticketOffice.getTicket();
        Long buy = audience.buy(ticket);
        ticketOffice.plusAmount(buy);

    }
}
