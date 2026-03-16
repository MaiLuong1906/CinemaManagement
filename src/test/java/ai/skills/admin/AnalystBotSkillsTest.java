package ai.skills.admin;

import model.ForecastDTO;
import model.ForecastResult;
import model.Movie_Ticket_ViewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.ForecastService;
import service.IncomeStatictisService;
import service.SeatFillRate_ViewService;
import service.TicketManagementService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AnalystBotSkillsTest {

    private AnalystBotSkills skills;

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
        skills = new AnalystBotSkills(incomeService, ticketService, seatService, forecastService);
    }

    @Test
    public void testGetRevenueSummary() {
        when(incomeService.getDaylyRevenue()).thenReturn(100.0);
        when(incomeService.calculateTotalRevenue()).thenReturn(3000.0);
        when(incomeService.getYearlyRevenue()).thenReturn(50000.0);

        String summary = skills.getRevenueSummary();
        
        assertTrue(summary.contains("100"));
        assertTrue(summary.contains("3.000"));
        assertTrue(summary.contains("50.000"));
    }

    @Test
    public void testGetTopPerformingMovies() {
        Movie_Ticket_ViewDTO m1 = new Movie_Ticket_ViewDTO();
        m1.setTitle("Movie A");
        m1.setTicketsSold(10);
        m1.setRevenue(1000.0);
        
        when(ticketService.getAllOfPageNumber(1)).thenReturn(Collections.singletonList(m1));

        String result = skills.getTopPerformingMovies();
        
        assertTrue(result.contains("Movie A"));
        assertTrue(result.contains("10 vé"));
        assertTrue(result.contains("1.000 VND"));
    }

    @Test
    public void testGet7DayForecast() {
        ForecastDTO future = new ForecastDTO();
        future.setFuture(true);
        future.setForecastRevenue(1000.0);
        future.setForecastTickets(10);
        
        ForecastResult mockResult = new ForecastResult(Collections.singletonList(future), "Healthy growth");
        when(forecastService.get7DayForecast()).thenReturn(mockResult);

        String forecast = skills.get7DayForecast();
        
        assertTrue(forecast.contains("1.000 VND"));
        assertTrue(forecast.contains("10 vé"));
        assertTrue(forecast.contains("Healthy growth"));
    }
}
