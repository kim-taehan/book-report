package developx.lecture.objects.ch1.v2;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("v2 테스트")
class TheaterTest {

    @DisplayName("초대장이 없는 관람객은 현금으로 구매할 수 있다. ")
    @Test
    void enter(){
        // given
        TicketOffice ticketOffice = new TicketOffice(1000L, new Ticket(2000L), new Ticket(2000L));
        TicketSeller ticketSeller = new TicketSeller(ticketOffice);
        Theater theater = new Theater(ticketSeller);

        // when
        Bag bag = new Bag(10000L);
        theater.enter(new Audience(bag));

        // then
        Assertions.assertThat(ticketOffice.getAmount()).isEqualTo(3000L);
        Assertions.assertThat(bag.getAmount()).isEqualTo(8000L);
    }


    @DisplayName("초대장이 있는 관람객은 초대장으로 구매할 수 있다. ")
    @Test
    void enterByInvitation(){
        // given
        developx.lecture.objects.ch1.v2.TicketOffice ticketOffice = new TicketOffice(1000L, new developx.lecture.objects.ch1.v2.Ticket(2000L), new Ticket(2000L));
        developx.lecture.objects.ch1.v2.TicketSeller ticketSeller = new TicketSeller(ticketOffice);
        developx.lecture.objects.ch1.v2.Theater theater = new Theater(ticketSeller);

        // when
        developx.lecture.objects.ch1.v2.Bag bag = new Bag(new Invitation(), 10000L);
        theater.enter(new Audience(bag));

        // then
        Assertions.assertThat(ticketOffice.getAmount()).isEqualTo(1000L);
        Assertions.assertThat(bag.getAmount()).isEqualTo(10000L);
    }

}