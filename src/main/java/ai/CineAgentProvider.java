package ai;

import ai.skills.admin.AnalystBotSkills;
import ai.skills.admin.MarketingBotSkills;
import ai.skills.admin.ModerateBotSkills;
import ai.skills.user.BookBotSkills;
import ai.skills.user.InfoBotSkills;
import dao.ChatMessageDAO;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import util.ConfigLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lớp cung cấp các Agent chuyên biệt sử dụng LangChain4j.
 * Đã hỗ trợ xoay tua API Key và tối ưu hóa Model theo tác vụ.
 */
public class CineAgentProvider {

    // Model constants
    public static final String FAST_MODEL = "llama3-8b-8192";           // Cực nhanh, rẻ, dùng cho query đơn giản
    public static final String VERSATILE_MODEL = "llama-3.3-70b-versatile"; // Cân bằng, dùng cho chatbot chính
    public static final String THINKING_MODEL = "deepseek-r1-distill-llama-70b"; // Thinking model cho Analyst
    
    private static final String GROQ_URL = "https://api.groq.com/openai/v1";

    /**
     * Quản lý xoay tua API Key.
     */
    private static class ApiKeyManager {
        private static final List<String> keys = new ArrayList<>();
        private static final AtomicInteger currentIndex = new AtomicInteger(0);

        static {
            String rawKeys = ConfigLoader.get("ai.api.key");
            if (rawKeys != null) {
                for (String k : rawKeys.split(",")) {
                    if (!k.trim().isEmpty()) keys.add(k.trim());
                }
            }
            if (keys.isEmpty()) {
                System.err.println("[AI-WARN] No API Keys found! AI services will fail.");
            }
        }

        public static String getNextKey() {
            if (keys.isEmpty()) return "";
            int index = currentIndex.getAndIncrement() % keys.size();
            return keys.get(index);
        }

        public static int getKeyCount() {
            return keys.size();
        }
    }

    /**
     * Giao diện chung cho các AI Agent.
     */
    public interface CineAgent {
        String chat(@MemoryId String memoryId, @UserMessage String userMessage);
    }

    /**
     * Giao diện cho Streaming AI Agent.
     */
    public interface StreamingCineAgent {
        dev.langchain4j.service.TokenStream chat(@MemoryId String memoryId, @UserMessage String userMessage);
    }

    /**
     * Khởi tạo Agent cho người dùng cuối (Customer).
     * Sử dụng VERSATILE_MODEL để đảm bảo chất lượng phản hồi.
     */
    public static CineAgent createUserAgent(int userId) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(VERSATILE_MODEL)
                .build();

        return AiServices.builder(CineAgent.class)
                .chatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new InfoBotSkills(), new BookBotSkills(userId))
                .systemMessageProvider(chatId -> 
                    "Bạn là CineGuide, một trợ lý rạp phim thông minh. " +
                    "Bạn có 2 bộ phận hỗ trợ:\n" +
                    "1. InfoBot: Tra cứu phim, suất chiếu, giá combo.\n" +
                    "2. BookBot: Hỗ trợ đặt vé, xem lịch sử và chuẩn bị xác nhận đặt vé.\n" +
                    "Khi khách hàng muốn đặt vé, hãy dùng BookBot để lấy sơ đồ ghế, sau đó dùng tool prepareBooking để hiển thị xác nhận."
                )
                .build();
    }

    /**
     * Khởi tạo Agent cho quản trị viên (Admin).
     * Sử dụng THINKING_MODEL cho AnalystBot để phân tích dữ liệu chuyên sâu.
     */
    public static CineAgent createAdminAgent() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(THINKING_MODEL)
                .build();

        return AiServices.builder(CineAgent.class)
                .chatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new AnalystBotSkills(), new MarketingBotSkills(), new ModerateBotSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineAnalyst, trợ lý quản trị cấp cao dựa trên tư duy phân tích của DeepSeek. " +
                    "Bạn điều hành 3 chuyên gia:\n" +
                    "1. AnalystBot: Thống kê doanh thu, vé và dự báo.\n" +
                    "2. MarketingBot: Tạo nội dung quảng cáo dựa trên dữ liệu phim.\n" +
                    "3. ModerateBot: Kiểm soát người dùng, logs hệ thống và trạng thái phòng.\n" +
                    "Hãy cung cấp báo cáo chuyên sâu và chuyên nghiệp."
                )
                .build();
    }

    /**
     * Khởi tạo Streaming Agent cho người dùng cuối.
     */
    public static StreamingCineAgent createStreamingUserAgent(int userId) {
        dev.langchain4j.model.chat.StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(VERSATILE_MODEL)
                .build();

        return AiServices.builder(StreamingCineAgent.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new InfoBotSkills(), new BookBotSkills(userId))
                .systemMessageProvider(chatId -> 
                    "Bạn là CineGuide, một trợ lý rạp phim thông minh. Giúp người dùng tra cứu phim, lịch chiếu và đặt vé qua InfoBot và BookBot."
                )
                .build();
    }

    /**
     * Khởi tạo Streaming Agent cho quản trị viên.
     */
    public static StreamingCineAgent createStreamingAdminAgent() {
        dev.langchain4j.model.chat.StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(THINKING_MODEL)
                .build();

        return AiServices.builder(StreamingCineAgent.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new AnalystBotSkills(), new MarketingBotSkills(), new ModerateBotSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineAnalyst, trợ lý quản trị cấp cao. Hỗ trợ thống kê, marketing và điều hành hệ thống rạp phim."
                )
                .build();
    }

    /**
     * Utility method to get a fast model for simple operations.
     */
    public static OpenAiChatModel getFastModel() {
        return OpenAiChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(FAST_MODEL)
                .build();
    }
    /**
     * Helper for non-streaming Admin tasks (like Forecasting).
     */
    public static OpenAiChatModel createAdminAgentModel() {
        return OpenAiChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(THINKING_MODEL)
                .build();
    }
}
