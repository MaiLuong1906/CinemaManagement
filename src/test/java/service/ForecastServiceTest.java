package service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dao.InvoiceDAO;
import dao.TicketsSoldDAO;
import model.ForecastDTO;
import model.ForecastResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

public class ForecastServiceTest {

    private ForecastService service;

    @Mock
    private InvoiceDAO invoiceDAO;

    @Mock
    private TicketsSoldDAO ticketsSoldDAO;

    @Mock
    private ChatLanguageModel chatModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ForecastService(invoiceDAO, ticketsSoldDAO, chatModel);
    }

    @Test
    public void testGet7DayForecastSuccess() throws Exception {
        LocalDate today = LocalDate.now();
        Map<LocalDate, Double> revenueHistory = new HashMap<>();
        Map<LocalDate, Integer> ticketHistory = new TreeMap<>();
        
        // Setup some dummy history
        for (int i = 0; i < 30; i++) {
            LocalDate d = today.minusDays(i);
            revenueHistory.put(d, 100.0);
            ticketHistory.put(d, 10);
        }

        when(invoiceDAO.getDailyRevenueHistory(30)).thenReturn(revenueHistory);
        when(ticketsSoldDAO.getDailyTicketHistory(30)).thenReturn(ticketHistory);

        // Mock AI response
        String aiJsonResponse = "{ \"data\": [{\"date\":\"" + today.plusDays(1) + "\", \"rev\":150, \"tix\":15}], \"analysis\": \"Growth expected\" }";
        when(chatModel.generate(any(dev.langchain4j.data.message.ChatMessage.class), any(dev.langchain4j.data.message.ChatMessage.class)))
            .thenReturn(Response.from(AiMessage.from(aiJsonResponse)));

        ForecastResult result = service.get7DayForecast();

        assertNotNull(result);
        assertFalse(result.getDailyData().isEmpty());
        assertEquals("Growth expected", result.getAnalysis());
        
        // Check if forecast data is present
        boolean foundFuture = false;
        for (ForecastDTO dto : result.getDailyData()) {
            if (dto.isFuture()) {
                foundFuture = true;
                assertEquals(150.0, dto.getForecastRevenue());
                assertEquals(15, dto.getForecastTickets());
                assertEquals(today.plusDays(1), dto.getDate());
            }
        }
        assertTrue(foundFuture);
    }

    @Test
    public void testGet7DayForecastFallbackOnException() throws Exception {
        // Mock getDailyRevenueHistory to succeed so we reach the try-catch block
        LocalDate today = LocalDate.now();
        Map<LocalDate, Double> revenueHistory = new HashMap<>();
        for (int i = 0; i < 30; i++) revenueHistory.put(today.minusDays(i), 100.0);
        when(invoiceDAO.getDailyRevenueHistory(30)).thenReturn(revenueHistory);
        
        // Throw exception during ticket history fetch which is inside the try-catch block
        when(ticketsSoldDAO.getDailyTicketHistory(30)).thenThrow(new RuntimeException("DB Error"));

        ForecastResult result = service.get7DayForecast();

        assertNotNull(result);
        assertEquals("Dữ liệu dự báo đang được cập nhật...", result.getAnalysis());
        assertFalse(result.getDailyData().isEmpty());
        // Verify historical part of fallback has 7 items as per code
        assertEquals(7, result.getDailyData().size());
    }
}
