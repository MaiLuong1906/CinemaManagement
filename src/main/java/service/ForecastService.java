package service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.InvoiceDAO;
import dao.TicketsSoldDAO;
import model.ForecastDTO;
import model.ForecastResult;
import util.ConfigLoader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ForecastService: Handles data aggregation for forecasting.
 * Intelligence logic is mirrored here for UI use, while the Agent uses AnalystBotSkills.
 */
public class ForecastService {
    private final InvoiceDAO invoiceDAO;
    private final TicketsSoldDAO ticketsSoldDAO;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ChatLanguageModel model;

    public ForecastService() {
        this.invoiceDAO = new InvoiceDAO();
        this.ticketsSoldDAO = new TicketsSoldDAO();
        this.model = OpenAiChatModel.builder()
                .apiKey(ConfigLoader.get("ai.api.key"))
                .baseUrl("https://api.groq.com/openai/v1")
                .modelName("llama-3.3-70b-versatile")
                .build();
    }

    public ForecastService(InvoiceDAO invoiceDAO, TicketsSoldDAO ticketsSoldDAO, ChatLanguageModel model) {
        this.invoiceDAO = invoiceDAO;
        this.ticketsSoldDAO = ticketsSoldDAO;
        this.model = model;
    }

    /**
     * Primary method for UI Dashboard.
     * Delegates reasoning to the same Prompt patterns used by CineAnalyst.
     */
    public ForecastResult get7DayForecast() {
        LocalDate today = LocalDate.now();
        Map<LocalDate, Double> revenueHistory = invoiceDAO.getDailyRevenueHistory(30);
        Map<LocalDate, Integer> ticketHistory;
        try {
            ticketHistory = ticketsSoldDAO.getDailyTicketHistory(30);
        } catch (Exception e) {
            ticketHistory = new TreeMap<>();
        }

        StringBuilder dataForAI = new StringBuilder();
        for (int i = 29; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            dataForAI.append(String.format("%s: %.0f, %d\n", d, revenueHistory.getOrDefault(d, 0.0), ticketHistory.getOrDefault(d, 0)));
        }

        String systemPrompt = "Bạn là chuyên gia phân tích rạp phim. Dự báo 7 ngày tới. Trả về JSON: { \"data\": [{\"date\":\"...\", \"rev\":0, \"tix\":0}], \"analysis\": \"...\" }";
        
        try {
            Response<AiMessage> response = model.generate(new SystemMessage(systemPrompt), new UserMessage(dataForAI.toString()));
            String llmResponse = response.content().text();
            
            // Basic extraction
            int start = llmResponse.indexOf("{");
            int end = llmResponse.lastIndexOf("}");
            if (start != -1 && end != -1) llmResponse = llmResponse.substring(start, end + 1);

            JsonNode root = mapper.readTree(llmResponse);
            List<ForecastDTO> dailyData = new ArrayList<>();
            
            // Historical (14 days)
            for (int i = 13; i >= 0; i--) {
                LocalDate d = today.minusDays(i);
                dailyData.add(new ForecastDTO(d, revenueHistory.getOrDefault(d, 0.0), ticketHistory.getOrDefault(d, 0), false));
            }

            // Forecast
            if (root.has("data") && root.get("data").isArray()) {
                for (JsonNode node : root.get("data")) {
                    ForecastDTO dto = new ForecastDTO();
                    dto.setDate(LocalDate.parse(node.get("date").asText()));
                    dto.setForecastRevenue(node.get("rev").asDouble());
                    dto.setForecastTickets(node.get("tix").asInt());
                    dto.setFuture(true);
                    dailyData.add(dto);
                }
            }
            
            return new ForecastResult(dailyData, root.get("analysis").asText());

        } catch (Exception e) {
            // Fallback to avoid breaking UI
            List<ForecastDTO> fallback = new ArrayList<>();
            for (int i = 6; i >= 0; i--) fallback.add(new ForecastDTO(today.minusDays(i), 0, 0, false));
            return new ForecastResult(fallback, "Dữ liệu dự báo đang được cập nhật...");
        }
    }
}
