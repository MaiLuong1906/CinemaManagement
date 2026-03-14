package ai.skills;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import model.Movie_Ticket_ViewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.IncomeStatictisService;
import service.SeatFillRate_ViewService;
import service.TicketManagementService;
import service.TimeSlotService;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class AdminSkillsTest {

    @Mock
    private IncomeStatictisService incomeService;
    @Mock
    private TicketManagementService ticketService;
    @Mock
    private TimeSlotService timeSlotService;
    @Mock
    private SeatFillRate_ViewService seatService;

    private AdminSkills adminSkills;

    @BeforeEach
    void setUp() {
        adminSkills = new AdminSkills(incomeService, ticketService, timeSlotService, seatService);
    }

    @Test
    void testGetRevenueSummary() {
        when(incomeService.getDaylyRevenue()).thenReturn(1000000.0);
        when(incomeService.calculateTotalRevenue()).thenReturn(50000000.0);
        when(incomeService.calculateTicketRevenue()).thenReturn(30000000.0);
        when(incomeService.calculateProductRevenue()).thenReturn(20000000.0);
        when(incomeService.getYearlyRevenue()).thenReturn(600000000.0);

        String result = adminSkills.getRevenueSummary();

        assertTrue(result.contains("1.000.000 VND"));
        assertTrue(result.contains("50.000.000 VND"));
        assertTrue(result.contains("30.000.000"));
        assertTrue(result.contains("20.000.000"));
    }

    @Test
    void testGetTopPerformingMovies() throws Exception {
        Movie_Ticket_ViewDTO m1 = new Movie_Ticket_ViewDTO(1, "Phim A", 100, 10000000, 1, 1);
        when(ticketService.getAllOfPageNumber(1)).thenReturn(Arrays.asList(m1));

        String result = adminSkills.getTopPerformingMovies();

        assertTrue(result.contains("Phim A"));
        assertTrue(result.contains("100 vé"));
        assertTrue(result.contains("10.000.000 VND"));
    }

    @Test
    void testGetMonthlySeatFillRate() throws Exception {
        when(seatService.getSeatFillRateCurrentMonth()).thenReturn(0.7552);

        String result = adminSkills.getMonthlySeatFillRate();

        assertTrue(result.contains("75,52%"));
    }

    @Test
    void testGetTicketSalesStats() throws Exception {
        when(ticketService.getDailyTicketsSold()).thenReturn(50);
        when(ticketService.getMothlyTicketsSold()).thenReturn(1500);
        when(ticketService.getYearlyTicketsSold()).thenReturn(18000);

        String result = adminSkills.getTicketSalesStats();

        assertTrue(result.contains("50 vé"));
        assertTrue(result.contains("1.500 vé"));
        assertTrue(result.contains("18.000 vé"));
    }
}
