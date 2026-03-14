package ai;

import ai.skills.AdminSkills;
import ai.skills.UserSkills;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
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
        String chat(String userMessage);
    }

    /**
     * Khởi tạo Agent cho người dùng cuối (Customer).
     */
    public static CineAgent createUserAgent() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(GROQ_URL)
                .modelName(MODEL_NAME)
                .build();

        return AiServices.builder(CineAgent.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(new UserSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineGuide, một trợ lý chuyên nghiệp tại rạp phim. " +
                    "Nhiệm vụ của bạn là hỗ trợ khách hàng tìm phim, xem lịch chiếu và giải đáp thắc mắc. " +
                    "Hãy luôn lịch sự và sử dụng các công cụ được cung cấp để lấy dữ liệu chính xác nhất."
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
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(new AdminSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineAnalyst, chuyên gia phân tích dữ liệu rạp phim. " +
                    "Bạn hỗ trợ Admin bằng cách cung cấp thông tin doanh thu, tỉ lệ lấp đầy và hiệu suất phim. " +
                    "Hãy trả lời một cách súc tích, chuyên nghiệp và dựa trên số liệu thực tế từ các công cụ."
                )
                .build();
    }
}
