package developx.lecture.objects.ch1.v1;

import lombok.Getter;

@Getter
public class TicketSeller {

    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }


}
