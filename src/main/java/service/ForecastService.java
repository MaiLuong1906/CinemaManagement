package service;

import ai.LLM;
import ai.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.InvoiceDAO;
import dao.TicketsSoldDAO;
import model.ForecastDTO;
import model.ForecastResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ForecastService {
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final TicketsSoldDAO ticketsSoldDAO = new TicketsSoldDAO();
    private final LLM llm = new LLM();
    private final ObjectMapper mapper = new ObjectMapper();

    public ForecastResult get7DayForecast() {
        // 1. Fetch 30 days of historical data
        Map<LocalDate, Double> revenueHistory = invoiceDAO.getDailyRevenueHistory(30);
        Map<LocalDate, Integer> ticketHistory;
        try {
            ticketHistory = ticketsSoldDAO.getDailyTicketHistory(30);
        } catch (Exception e) {
            ticketHistory = new TreeMap<>();
        }

        // 2. Prepare data for LLM
        StringBuilder dataForLLM = new StringBuilder("Dữ liệu 30 ngày qua (Ngày, Doanh thu, Số vé):\n");
        LocalDate today = LocalDate.now();
        for (int i = 29; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            double rev = revenueHistory.getOrDefault(d, 0.0);
            int tix = ticketHistory.getOrDefault(d, 0);
            dataForLLM.append(String.format("%s: %.0f, %d\n", d, rev, tix));
        }

        // 3. Build Prompt
        String systemPrompt = """
            Bạn là một chuyên gia phân tích dữ liệu rạp chiếu phim. 
            Nhiệm vụ: Dự báo doanh thu và số vé cho 7 ngày TỚI dựa trên dữ liệu 30 ngày qua.
            TÍNH CHẤT DỮ LIỆU:
            - Nếu xu hướng gần đây sụt giảm hoặc bằng 0, hãy dự báo sát với thực tế đó (không được phép dự báo tăng vọt vô lý).
            - Ưu tiên bám sát xu hướng của 7 ngày gần nhất.
            
            YÊU CẦU ĐỊNH DẠNG:
            Kết quả trả về CHỈ bao gồm một đối tượng JSON với 2 trường:
            - "data": Mảng 7 đối tượng cho 7 ngày tiếp theo (mỗi đối tượng có: "date" (YYYY-MM-DD), "rev" (number), "tix" (int)).
            - "analysis": Một đoạn văn ngắn (tối đa 100 từ) giải thích lý do tại sao bạn đưa ra con số dự báo như vậy.
            
            Không giải thích gì thêm ngoài JSON.
            """;

        String userPrompt = dataForLLM.toString() + "\nNgày hiện tại: " + today + "\nHãy dự báo cho 7 ngày tiếp theo và đưa ra nhận xét ngắn gọn.";

        // 4. Call LLM
        try {
            System.out.println("DEBUG - ForecastService - Calling LLM...");
            String llmResponse = llm.generate(List.of(
                    new Message("system", systemPrompt),
                    new Message("user", userPrompt)
            ));

            // Clean response: Find the first '{' and last '}'
            int start = llmResponse.indexOf("{");
            int end = llmResponse.lastIndexOf("}");
            if (start != -1 && end != -1 && end > start) {
                llmResponse = llmResponse.substring(start, end + 1);
            }
            llmResponse = llmResponse.trim();

            JsonNode root = mapper.readTree(llmResponse);
            List<ForecastDTO> dailyData = new ArrayList<>();
            String analysis = "";
            if (root.has("analysis")) {
                analysis = root.get("analysis").asText();
            }

            // Add historical data (last 14 days)
            for (int i = 13; i >= 0; i--) {
                LocalDate d = today.minusDays(i);
                dailyData.add(new ForecastDTO(d, revenueHistory.getOrDefault(d, 0.0), ticketHistory.getOrDefault(d, 0), false));
            }

            // Add forecasted data
            boolean hasFuture = false;
            JsonNode dataNode = root.has("data") ? root.get("data") : (root.isArray() ? root : null);
            
            if (dataNode != null && dataNode.isArray()) {
                for (JsonNode node : dataNode) {
                    if (node.has("date") && (node.has("rev") || node.has("tix"))) {
                        ForecastDTO dto = new ForecastDTO();
                        dto.setDate(LocalDate.parse(node.get("date").asText()));
                        dto.setForecastRevenue(node.has("rev") ? node.get("rev").asDouble() : 0);
                        dto.setForecastTickets(node.has("tix") ? node.get("tix").asInt() : 0);
                        dto.setFuture(true);
                        dailyData.add(dto);
                        hasFuture = true;
                    }
                }
            }

            // Fallback: Weighted Moving Average
            if (!hasFuture) {
                System.out.println("DEBUG - ForecastService - Using Fallback Trend");
                analysis = "Dự báo dựa trên xu hướng lịch sử thực tế.";
                double weightedSumRev = 0;
                double weightedSumTix = 0;
                int totalWeight = 0;
                for (int i = 0; i < 14; i++) {
                    LocalDate d = today.minusDays(i);
                    int weight = (14 - i);
                    weightedSumRev += revenueHistory.getOrDefault(d, 0.0) * weight;
                    weightedSumTix += ticketHistory.getOrDefault(d, 0) * weight;
                    totalWeight += weight;
                }
                double avgRev = weightedSumRev / totalWeight;
                double avgTix = weightedSumTix / totalWeight;

                for (int i = 1; i <= 7; i++) {
                    LocalDate d = today.plusDays(i);
                    ForecastDTO dto = new ForecastDTO();
                    dto.setDate(d);
                    dto.setForecastRevenue(avgRev * (0.9 + Math.random() * 0.2));
                    dto.setForecastTickets((int)(avgTix * (0.9 + Math.random() * 0.2)));
                    dto.setFuture(true);
                    dailyData.add(dto);
                }
            }
            return new ForecastResult(dailyData, analysis);

        } catch (Exception e) {
            System.err.println("ERROR - ForecastService: " + e.getMessage());
            
            List<ForecastDTO> fallbackData = new ArrayList<>();
            for (int i = 13; i >= 0; i--) {
                LocalDate d = today.minusDays(i);
                fallbackData.add(new ForecastDTO(d, revenueHistory.getOrDefault(d, 0.0), ticketHistory.getOrDefault(d, 0), false));
            }
            return new ForecastResult(fallbackData, "Lỗi hệ thống AI: " + e.getMessage());
        }
    }
}
