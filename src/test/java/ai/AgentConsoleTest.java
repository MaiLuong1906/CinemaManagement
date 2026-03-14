package ai;

import ai.CineAgentProvider;
import java.util.Scanner;

/**
 * Lớp test tương tác trực tiếp với AI Agent qua Console.
 * Dùng để kiểm tra nhanh luồng Tools và LLM.
 */
public class AgentConsoleTest {

    public static void main(String[] args) {
        System.out.println("=== Cinematic Agent Console Test ===");
        System.out.println("1. Test UserAgent (CineGuide)");
        System.out.println("2. Test AdminAgent (CineAnalyst)");
        System.out.print("Chọn loại agent (1/2): ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        CineAgentProvider.CineAgent agent;
        if ("2".equals(choice)) {
            System.out.println("--- Khởi tạo AdminAgent ---");
            agent = CineAgentProvider.createAdminAgent();
        } else {
            System.out.println("--- Khởi tạo UserAgent ---");
            agent = CineAgentProvider.createUserAgent();
        }

        System.out.println("Agent đã sẵn sàng. Nhập 'exit' để thoát.");

        while (true) {
            System.out.print("\nUser: ");
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) break;

            try {
                long start = System.currentTimeMillis();
                String response = agent.chat(input);
                long end = System.currentTimeMillis();
                
                System.out.println("\nAI: " + response);
                System.out.println("\n[Thời gian xử lý: " + (end - start) + "ms]");
            } catch (Exception e) {
                System.err.println("Lỗi: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
        System.out.println("Thoát chương trình.");
    }
}
