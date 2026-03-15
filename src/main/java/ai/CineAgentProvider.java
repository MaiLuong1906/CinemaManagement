package ai;

import ai.skills.admin.AnalystBotSkills;
import ai.skills.admin.MarketingBotSkills;
import ai.skills.admin.ModerateBotSkills;
import ai.skills.user.BookBotSkills;
import ai.skills.user.InfoBotSkills;
import dao.ChatMessageDAO;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import utils.ConfigLoader;

/**
 * Lớp cung cấp các Agent chuyên biệt sử dụng LangChain4j.
 */
public class CineAgentProvider {

    private static final String API_KEY = ConfigLoader.get("ai.api.key");
    private static final String MODEL_NAME = "llama-3.3-70b-versatile";
    private static final String GROQ_URL = "https://api.groq.com/openai/v1";

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
     */
    public static CineAgent createUserAgent(int userId) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(GROQ_URL)
                .modelName(MODEL_NAME)
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
     */
    public static CineAgent createAdminAgent() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(GROQ_URL)
                .modelName(MODEL_NAME)
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
                    "Bạn là CineAnalyst, trợ lý quản trị cấp cao. " +
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
        dev.langchain4j.model.chat.StreamingChatLanguageModel model = dev.langchain4j.model.openai.OpenAiStreamingChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(GROQ_URL)
                .modelName(MODEL_NAME)
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
        dev.langchain4j.model.chat.StreamingChatLanguageModel model = dev.langchain4j.model.openai.OpenAiStreamingChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(GROQ_URL)
                .modelName(MODEL_NAME)
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
}
