package service;

import dao.InvoiceDAO;
import dao.TicketsSoldDAO;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import model.ForecastResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.TreeMap;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ForecastServiceTest {

    private ForecastService forecastService;

    @Mock
    private InvoiceDAO invoiceDAO;

    @Mock
    private TicketsSoldDAO ticketsSoldDAO;

    @Mock
    private ChatLanguageModel model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        forecastService = new ForecastService(invoiceDAO, ticketsSoldDAO, model);
    }

    @Test
    public void testGet7DayForecastSuccess() throws Exception {
        // Setup mocks
        when(invoiceDAO.getDailyRevenueHistory(anyInt())).thenReturn(new TreeMap<>());
        when(ticketsSoldDAO.getDailyTicketHistory(anyInt())).thenReturn(new TreeMap<>());

        String mockJsonResponse = "{ \"data\": [{\"date\":\"" + LocalDate.now().plusDays(1) + "\", \"rev\":1000, \"tix\":10}], \"analysis\": \"Good growth\" }";
        Response<AiMessage> mockResponse = Response.from(AiMessage.from(mockJsonResponse));
        
        // Mock the specific signature used in code: generate(ChatMessage...) 
        // which Mockito sees as generate(SystemMessage, UserMessage) or similar if specific types are used
        when(model.generate(any(SystemMessage.class), any(UserMessage.class))).thenReturn(mockResponse);

        // Execute
        ForecastResult result = forecastService.get7DayForecast();

        // Verify
        assertNotNull(result);
        assertEquals("Good growth", result.getAnalysis());
        assertFalse(result.getDailyData().isEmpty());
    }

    @Test
    public void testGet7DayForecastFallback() throws Exception {
        // Setup mock to throw exception
        when(model.generate(any(SystemMessage.class), any(UserMessage.class))).thenThrow(new RuntimeException("AI Error"));

        // Execute
        ForecastResult result = forecastService.get7DayForecast();

        // Verify fallback behavior
        assertNotNull(result);
        assertEquals("Dữ liệu dự báo đang được cập nhật...", result.getAnalysis());
        assertTrue(result.getDailyData().size() >= 7);
    }
}
