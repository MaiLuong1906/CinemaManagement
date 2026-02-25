package ai;

import ai.memory.Memory;
import ai.agent.FillRateEnvironment;
import service.SeatFillRate_ViewService;

/**
 * Test console cho chatbot tư vấn độ phủ ghế
 */
public class AgentConsoleTest {

    public static void main(String[] args) {

        // Khởi tạo các thành phần
        LLM llm = new LLM();
        IntentClassifier intentClassifier = new IntentClassifier(llm);
        Memory memory = new Memory();

        // FillRateEnvironment nhận SeatFillRate_ViewService thực tế
        SeatFillRate_ViewService seatService = new SeatFillRate_ViewService();
        FillRateEnvironment fillRateEnv = new FillRateEnvironment(seatService);

        // Khởi tạo Agent
        Agent agent = new Agent(intentClassifier, memory, llm, fillRateEnv);

        System.out.println("=== Chatbot Tư Vấn Độ Phủ Ghế ===\n");

        // Test 1: Small Talk
        System.out.println("--- Test 1: Small Talk ---");
        System.out.println("User: Xin chào, bạn có thể giúp gì cho tôi?");
        System.out.println("Bot: " + agent.handle("Xin chào, bạn có thể giúp gì cho tôi?"));
        System.out.println();

        // Test 2: Hỏi tổng quan độ phủ ghế
        System.out.println("--- Test 2: Độ phủ ghế tổng thể ---");
        System.out.println("User: Tháng này độ phủ ghế của rạp bao nhiêu?");
        System.out.println("Bot: " + agent.handle("Tháng này độ phủ ghế của rạp bao nhiêu?"));
        System.out.println();

        // Test 3: Hỏi theo suất chiếu
        System.out.println("--- Test 3: Độ phủ ghế theo suất chiếu ---");
        System.out.println("User: Cho tôi xem độ phủ ghế từng suất chiếu");
        System.out.println("Bot: " + agent.handle("Cho tôi xem độ phủ ghế từng suất chiếu"));
        System.out.println();

        // Test 4: Phân tích và tư vấn định hướng kinh tế
        System.out.println("--- Test 4: Phân tích và định hướng kinh tế ---");
        System.out.println("User: Phân tích tình hình và đưa ra giải pháp cải thiện giúp tôi");
        System.out.println("Bot: " + agent.handle("Phân tích tình hình và đưa ra giải pháp cải thiện giúp tôi"));
        System.out.println();

        // Test 5: Hỏi về khái niệm fill rate
        System.out.println("--- Test 5: Khái niệm fill rate ---");
        System.out.println("User: Fill rate là gì và tại sao nó quan trọng?");
        System.out.println("Bot: " + agent.handle("Fill rate là gì và tại sao nó quan trọng?"));
        System.out.println();

        // Test 6: Ngoài domain
        System.out.println("--- Test 6: Câu hỏi ngoài domain ---");
        System.out.println("User: Bạn có thể dạy tôi nấu ăn không?");
        System.out.println("Bot: " + agent.handle("Bạn có thể dạy tôi nấu ăn không?"));
    }
}