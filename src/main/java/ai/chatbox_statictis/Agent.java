package ai;

import ai.memory.Memory;
import model.SeatFillRate_ViewDTO;
import service.SeatFillRate_ViewService;
import java.util.List;

public class Agent {

    private final Memory memory;
    private final LLM llm;
    private final SeatFillRate_ViewService seatService;

    private static final String SYSTEM_PROMPT = """
    Bạn là chuyên gia tư vấn kinh tế cho rạp chiếu phim, chuyên phân tích độ phủ ghế (seat fill rate).
    
    Khi phân tích, hãy dựa trên ngưỡng sau:
    - Dưới 40%: Kém — cần can thiệp ngay
    - 40% - 60%: Trung bình — có thể cải thiện
    - 60% - 80%: Tốt — duy trì và tối ưu thêm
    - Trên 80%: Xuất sắc — cân nhắc mở rộng
    
    Quy tắc bắt buộc:
    - Nếu dữ liệu trống hoặc không đủ: nói rõ "Hiện không có đủ dữ liệu để phân tích" và đề nghị kiểm tra lại.
    - Nếu câu hỏi không liên quan đến rạp chiếu phim hoặc độ phủ ghế: lịch sự từ chối và gợi ý 2 câu hỏi mẫu người dùng có thể hỏi.
    - Trả lời bằng tiếng Việt, rõ ràng, súc tích, thực tế.
    """;

    public Agent(Memory memory, LLM llm, SeatFillRate_ViewService seatService) {
        this.memory = memory;
        this.llm = llm;
        this.seatService = seatService;
    }
    private String buildPrompt(String userMessage, String dataContext) {
    return String.format("""
        Dữ liệu độ phủ ghế tháng hiện tại:
        %s
        
        Lịch sử hội thoại:
        %s
        
        Câu hỏi của người dùng: "%s"
        
        Lưu ý:
        - Nếu dữ liệu trên trống hoặc có thông báo lỗi: trả lời "Hiện không có đủ dữ liệu để phân tích."
        - Nếu câu hỏi không liên quan đến độ phủ ghế hoặc rạp chiếu phim: lịch sự từ chối và gợi ý người dùng hỏi đúng chủ đề.
        - Ngược lại: phân tích dữ liệu và trả lời trực tiếp.
        """, dataContext, formatHistory(), userMessage);
}
    public String handle(String userMessage) {
    try {
        // 1. Lấy toàn bộ data từ DB
        String dataContext = fetchDataAsText();

        // 2. Build prompt
        String prompt = buildPrompt(userMessage, dataContext);

        // 3. Gửi lên Gemini với system prompt
        memory.addMessage("user", userMessage);
        String response = llm.generate(List.of(
            new Message("system", SYSTEM_PROMPT),
            new Message("user", prompt)
        ));

        memory.addMessage("assistant", response);
        return response;

    }  catch (Exception e) {
    String fallback = "Xin lỗi, tôi đang gặp sự cố kỹ thuật. " +
        "Bạn có thể hỏi tôi về độ phủ ghế, tỷ lệ lấp đầy hoặc hiệu suất suất chiếu nhé!" +
        "\n[DEBUG] " + e.getMessage(); // thêm dòng này tạm thời
    memory.addMessage("assistant", fallback);
    return fallback;
}
}

    /**
     * Lấy data từ DB và format thành text cho Gemini đọc
     */
    private String fetchDataAsText() {
        try {
            List<SeatFillRate_ViewDTO> data = seatService.getAllCurrentMonth();

            if (data.isEmpty()) {
                return "Không có dữ liệu trong tháng hiện tại.";
            }

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-12s %-20s %-10s %-20s %10s %10s %10s\n",
                "Ngày", "Phim", "Phòng", "Khung giờ", "Đã bán", "Tổng ghế", "Độ phủ%"));
            sb.append("-".repeat(95)).append("\n");

            for (SeatFillRate_ViewDTO d : data) {
                sb.append(String.format("%-12s %-20s %-10s %s-%s %10d %10d %9.2f%%\n",
                    d.getShowDate(),
                    truncate(d.getMovieTitle(), 20),
                    truncate(d.getHallName(), 10),
                    d.getStartTime(),
                    d.getEndTime(),
                    d.getTicketsSold(),
                    d.getTotalSeats(),
                    d.getFillRate()
                ));
            }

            return sb.toString();

        } catch (Exception e) {
            return "Lỗi khi lấy dữ liệu: " + e.getMessage();
        }
    }

    private String formatHistory() {
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

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    public void clearMemory() {
        memory.clear();
    }
}