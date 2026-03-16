package ai.skills.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.IncomeStatictisService;
import service.TicketManagementService;
import service.SeatFillRate_ViewService;
import service.ForecastService;
import model.Movie_Ticket_ViewDTO;
import model.ForecastResult;
import model.ForecastDTO;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class AnalystBotSkillsTest {

    private AnalystBotSkills analystBotSkills;

    @Mock
    private IncomeStatictisService incomeService;
    @Mock
    private TicketManagementService ticketService;
    @Mock
    private SeatFillRate_ViewService seatService;
    @Mock
    private ForecastService forecastService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        analystBotSkills = new AnalystBotSkills(incomeService, ticketService, seatService, forecastService);
    }

    @Test
    public void testGetRevenueSummary() {
        System.out.println("--- Testing getRevenueSummary (Mocked) ---");
        when(incomeService.getDaylyRevenue()).thenReturn(100.0);
        when(incomeService.calculateTotalRevenue()).thenReturn(5000.0);
        when(incomeService.getYearlyRevenue()).thenReturn(60000.0);

        String result = analystBotSkills.getRevenueSummary();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("100"));
        assertTrue(result.contains("5.000"));
        assertTrue(result.contains("60.000"));
    }

    @Test
    public void testGetTopPerformingMovies() {
        System.out.println("--- Testing getTopPerformingMovies (Mocked) ---");
        Movie_Ticket_ViewDTO m = new Movie_Ticket_ViewDTO();
        m.setTitle("Mock Top Movie");
        m.setTicketsSold(100);
        m.setRevenue(8000.0);

        when(ticketService.getAllOfPageNumber(1)).thenReturn(Arrays.asList(m));

        String result = analystBotSkills.getTopPerformingMovies();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Mock Top Movie"));
    }

    @Test
    public void testGet7DayForecast() {
        System.out.println("--- Testing get7DayForecast (Mocked) ---");
        ForecastDTO dto = new ForecastDTO();
        dto.setForecastRevenue(1000.0);
        dto.setForecastTickets(10);
        dto.setFuture(true);
        
        ForecastResult fr = new ForecastResult(Arrays.asList(dto), "Expect growth");
        when(forecastService.get7DayForecast()).thenReturn(fr);

        String result = analystBotSkills.get7DayForecast();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("1.000"));
        assertTrue(result.contains("Expect growth"));
    }
}
