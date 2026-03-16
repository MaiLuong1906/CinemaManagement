package ai;

import java.util.Scanner;

/**
 * Lớp test tương tác trực tiếp với AI Agent (CineGuide/CineAnalyst) qua Console.
 * Dùng để kiểm tra cấu trúc sub-agents và luồng booking JSON.
 */
public class AgentConsoleTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Cinematic Multi-Agent Console Test ===");
        System.out.println("Chọn role: 1. User (CineGuide) | 2. Admin (CineAnalyst)");
        String roleInput = scanner.nextLine();

        CineAgentProvider.CineAgent agent;
        if ("2".equals(roleInput)) {
            agent = CineAgentProvider.createAdminAgent();
            System.out.println("--- CineAnalyst (Admin) Online ---");
        } else {
            agent = CineAgentProvider.createUserAgent(1, null);
            System.out.println("--- CineGuide (User) Online ---");
        }

        while (true) {
            System.out.print("\nUser: ");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                break;
            }

            long start = System.currentTimeMillis();
            String response = agent.chat("console-session", input);
            long end = System.currentTimeMillis();

            System.out.println("\nAI: " + response);
            
            // Highlight structured JSON
            if (response.trim().startsWith("{") && response.contains("actionType")) {
                System.out.println("\n[DETECTED INTERACTIVE ACTION]");
            }
            
            System.out.println("\n(Time: " + (end - start) + "ms)");
        }

        scanner.close();
    }
}
