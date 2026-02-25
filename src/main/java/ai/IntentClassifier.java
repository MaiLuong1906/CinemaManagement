package ai;

import java.util.ArrayList;
import java.util.List;

/**
 * Phân loại ý định người dùng — chuyên cho chatbot tư vấn độ phủ ghế
 */
public class IntentClassifier {

    private final LLM llm;
    private final List<IntentDefinition> intents;

    public IntentClassifier(LLM llm) {
        this.llm = llm;
        this.intents = new ArrayList<>();
        registerDefaultIntents();
    }

    /**
     * Đăng ký các intent mặc định liên quan độ phủ ghế
     */
    private void registerDefaultIntents() {

        addIntent("QUERY_FILL_RATE_OVERALL",
                "Hỏi về độ phủ ghế tổng thể, tỷ lệ lấp đầy chung của tháng",
                "độ phủ ghế tháng này bao nhiêu, tỷ lệ lấp đầy hiện tại là bao nhiêu phần trăm");

        addIntent("QUERY_FILL_RATE_BY_SHOWTIME",
                "Hỏi về độ phủ ghế theo từng suất chiếu cụ thể",
                "suất chiếu nào lấp đầy nhất, cho tôi xem fill rate từng suất, độ phủ ghế theo suất");

        addIntent("ANALYZE_AND_ADVISE",
                "Yêu cầu phân tích toàn diện và đưa ra lời khuyên, định hướng kinh tế cải thiện",
                "phân tích giúp tôi, tình hình thế nào, có nên thay đổi gì không, tư vấn cải thiện độ phủ");

        addIntent("EXPLAIN_FILL_RATE",
                "Hỏi về khái niệm, ý nghĩa, cách tính của độ phủ ghế hoặc fill rate",
                "fill rate là gì, độ phủ ghế có nghĩa gì, tỷ lệ lấp đầy dùng để làm gì");

        addIntent("SMALL_TALK",
                "Chào hỏi, trò chuyện thông thường không liên quan nghiệp vụ",
                "xin chào, hello, cảm ơn, bạn là ai, bạn có thể làm gì");

        addIntent("UNKNOWN",
                "Câu hỏi hoàn toàn nằm ngoài chủ đề độ phủ ghế và rạp chiếu phim",
                "");
    }

    /**
     * Thêm intent tùy chỉnh từ bên ngoài
     */
    public void addIntent(String name, String description, String examples) {
        intents.add(new IntentDefinition(name, description, examples));
    }

    /**
     * Phân loại intent từ tin nhắn người dùng
     */
    public String classify(String userMessage) {

        StringBuilder intentList = new StringBuilder();
        for (IntentDefinition intent : intents) {
            intentList.append(intent.name).append(" - ").append(intent.description).append("\n");
            if (!intent.examples.isEmpty()) {
                intentList.append("  Ví dụ: ").append(intent.examples).append("\n");
            }
        }

        String prompt = String.format("""
        Bạn là hệ thống phân loại ý định cho chatbot tư vấn độ phủ ghế rạp chiếu phim.
        
        Danh sách intent:
        %s
        
        Tin nhắn người dùng: "%s"
        
        Quy tắc:
        - Trả về CHÍNH XÁC một tên intent trong danh sách
        - Ưu tiên intent cụ thể nhất phù hợp với câu hỏi
        - Nếu không liên quan đến rạp chiếu hoặc độ phủ ghế → UNKNOWN
        - KHÔNG giải thích, CHỈ trả về tên intent
        
        Intent:""", intentList.toString(), userMessage);

        String result = llm.generate(prompt).trim();

        // Validate — chỉ chấp nhận tên intent đã đăng ký
        for (IntentDefinition intent : intents) {
            if (result.equalsIgnoreCase(intent.name)) {
                return intent.name;
            }
        }

        return "UNKNOWN";
    }

    /**
     * Lấy danh sách tất cả intent đã đăng ký
     */
    public List<String> getRegisteredIntents() {
        List<String> names = new ArrayList<>();
        for (IntentDefinition intent : intents) {
            names.add(intent.name);
        }
        return names;
    }

    private static class IntentDefinition {
        String name;
        String description;
        String examples;

        IntentDefinition(String name, String description, String examples) {
            this.name = name;
            this.description = description;
            this.examples = examples;
        }
    }
}