package ai;

import ai.memory.Memory;
import ai.agent.FillRateEnvironment;
import model.SeatFillRate_ViewDTO;
import java.util.List;

/**
 * Agent tư vấn độ phủ ghế rạp chiếu phim.
 * Tiếp nhận câu hỏi, lấy dữ liệu từ FillRateEnvironment,
 * phân tích và đưa ra nhận xét + định hướng kinh tế.
 */
public class Agent {

    private final IntentClassifier intentClassifier;
    private final Memory memory;
    private final LLM llm;
    private final FillRateEnvironment fillRateEnv;

    private static final String DOMAIN_KEYWORDS =
            "độ phủ ghế, tỷ lệ lấp đầy, fill rate, suất chiếu, hiệu suất rạp";

    public Agent(IntentClassifier intentClassifier,
                 Memory memory,
                 LLM llm,
                 FillRateEnvironment fillRateEnv) {
        this.intentClassifier = intentClassifier;
        this.memory = memory;
        this.llm = llm;
        this.fillRateEnv = fillRateEnv;
    }

    /**
     * Xử lý tin nhắn từ người dùng
     */
    public String handle(String userMessage) {

        // 1. Lưu tin nhắn vào memory
        memory.addMessage("user", userMessage);

        // 2. Phân loại intent
        String intent = intentClassifier.classify(userMessage);

        // 3. Ngoài domain → steering
        if ("UNKNOWN".equals(intent)) {
            String response = generateSteering(userMessage);
            memory.addMessage("assistant", response);
            return response;
        }

        // 4. Small talk → trả lời ngắn gọn
        if ("SMALL_TALK".equals(intent)) {
            String response = handleSmallTalk(userMessage);
            memory.addMessage("assistant", response);
            return response;
        }

        // 5. Intent liên quan độ phủ ghế → lấy dữ liệu & tư vấn
        String response = handleFillRateIntent(userMessage, intent);
        memory.addMessage("assistant", response);
        return response;
    }

    /**
     * Xử lý các intent liên quan đến độ phủ ghế
     */
    private String handleFillRateIntent(String userMessage, String intent) {

        StringBuilder dataContext = new StringBuilder();

        try {
            switch (intent) {
                case "QUERY_FILL_RATE_OVERALL" -> {
                    double rate = fillRateEnv.getCurrentMonthSeatCoverage();
                    dataContext.append(String.format(
                        "Độ phủ ghế tháng hiện tại: %.2f%%\n", rate));
                }
                case "QUERY_FILL_RATE_BY_SHOWTIME" -> {
                    List<SeatFillRate_ViewDTO> data = fillRateEnv.getShowtimeCoverage();
                    dataContext.append("Độ phủ ghế theo từng suất chiếu:\n");
                    for (SeatFillRate_ViewDTO item : data) {
                        dataContext.append(String.format(
                            "- Suất %s: %.2f%% (tổng %d ghế)\n",
                            item.getShowtimeId(),
                            item.getFillRate(),
                            item.getTotalSeats()
                        ));
                    }
                }
                case "ANALYZE_AND_ADVISE" -> {
                    double overall = fillRateEnv.getCurrentMonthSeatCoverage();
                    List<SeatFillRate_ViewDTO> data = fillRateEnv.getShowtimeCoverage();
                    dataContext.append(String.format(
                        "Độ phủ ghế tháng hiện tại: %.2f%%\n\n", overall));
                    dataContext.append("Chi tiết theo từng suất chiếu:\n");
                    for (SeatFillRate_ViewDTO item : data) {
                        dataContext.append(String.format(
                            "- Suất %s: %.2f%% (tổng %d ghế)\n",
                            item.getShowtimeId(),
                            item.getFillRate(),
                            item.getTotalSeats()
                        ));
                    }
                }
                default -> {
                    // EXPLAIN_FILL_RATE hoặc intent khác không cần data thực
                    dataContext.append("Không cần truy vấn dữ liệu thực tế.\n");
                }
            }
        } catch (Exception e) {
            dataContext.append("Lỗi khi lấy dữ liệu: ").append(e.getMessage()).append("\n");
        }

        return synthesizeResponse(userMessage, dataContext.toString());
    }

    /**
     * Tổng hợp dữ liệu và tạo câu trả lời tư vấn
     */
    private String synthesizeResponse(String question, String data) {

        String prompt = String.format("""
        Bạn là chuyên gia tư vấn kinh tế cho rạp chiếu phim, chuyên về phân tích độ phủ ghế (seat fill rate).
        
        Câu hỏi: "%s"
        
        Dữ liệu:
        %s
        
        Lịch sử hội thoại gần đây:
        %s
        
        Hãy thực hiện:
        1. Trả lời trực tiếp câu hỏi dựa trên dữ liệu
        2. Nhận xét mức độ phủ ghế theo ngưỡng:
           - Dưới 40%%: Kém — cần can thiệp ngay
           - 40%% - 60%%: Trung bình — có thể cải thiện
           - 60%% - 80%%: Tốt — duy trì và tối ưu thêm
           - Trên 80%%: Xuất sắc — cân nhắc mở rộng
        3. Đề xuất 2-3 định hướng kinh tế cụ thể phù hợp với tình hình
           (ví dụ: điều chỉnh lịch chiếu, khuyến mãi giá vé, combo, tăng marketing cho suất yếu...)
        4. Nếu có suất chiếu yếu riêng lẻ, gợi ý hành động cải thiện cho suất đó
        
        Trả lời rõ ràng, súc tích và thực tế.
        """, question, data, formatConversationHistory());

        return llm.generate(prompt);
    }

    /**
     * Điều hướng khi câu hỏi ngoài domain
     */
    private String generateSteering(String userMessage) {

        String prompt = String.format("""
        Bạn là trợ lý tư vấn độ phủ ghế rạp chiếu phim.
        
        Người dùng vừa hỏi: "%s"
        
        Câu hỏi này nằm ngoài phạm vi hỗ trợ của bạn.
        
        Hãy:
        1. Lịch sự cho biết bạn chỉ hỗ trợ về: %s
        2. Đưa ra 2 câu hỏi mẫu người dùng có thể hỏi bạn
        
        Giọng điệu thân thiện, ngắn gọn.
        """, userMessage, DOMAIN_KEYWORDS);

        return llm.generate(prompt);
    }

    /**
     * Xử lý small talk
     */
    private String handleSmallTalk(String userMessage) {

        String prompt = String.format("""
        Bạn là trợ lý tư vấn độ phủ ghế rạp chiếu phim.
        
        Người dùng nói: "%s"
        
        Trả lời thân thiện, ngắn gọn. Sau đó gợi ý nhẹ nhàng rằng bạn có thể giúp phân tích %s.
        """, userMessage, DOMAIN_KEYWORDS);

        return llm.generate(prompt);
    }

    /**
     * Format 6 tin nhắn gần nhất từ memory
     */
    private String formatConversationHistory() {
        List<Message> messages = memory.getConversation();
        if (messages.isEmpty()) return "(Chưa có lịch sử)";

        StringBuilder sb = new StringBuilder();
        int limit = Math.max(0, messages.size() - 6);
        for (int i = limit; i < messages.size(); i++) {
            Message msg = messages.get(i);
            sb.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Reset memory
     */
    public void clearMemory() {
        memory.clear();
    }
}