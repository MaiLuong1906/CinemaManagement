// package ai;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

// /**
// * Integration tests using real Groq API and CineAgentProvider.
// * Verifies that the LLM can use InfoBot and BookBot skills to answer queries.
// */
// public class AgentRealApiTest {

// @Test
// public void testUserAgentWithInfoBot() throws InterruptedException {
// System.out.println("--- Testing UserAgent with InfoBot (Real API) ---");
// CineAgentProvider.CineAgent agent = CineAgentProvider.createUserAgent();
// Thread.sleep(15000); // 15 seconds to be safe with TPM

// // Query that requires searching for movies
// long start = System.currentTimeMillis();
// String response = agent.chat("test-session-info", "Rạp đang có những phim
// gì?");
// long end = System.currentTimeMillis();

// System.out.println("AI Response: " + response);
// System.out.println("Time taken: " + (end - start) + "ms");

// assertNotNull(response);
// assertFalse(response.isEmpty());
// assertTrue(response.length() > 20);
// }

// @Test
// public void testUserAgentWithBookBot() throws InterruptedException {
// System.out.println("--- Testing UserAgent with BookBot (Real API) ---");
// CineAgentProvider.CineAgent agent = CineAgentProvider.createUserAgent();
// Thread.sleep(15000);

// // Query that requires getting a seat map
// long start = System.currentTimeMillis();
// String response = agent.chat("test-session-book", "Cho tôi xem sơ đồ ghế của
// suất chiếu ID 1");
// long end = System.currentTimeMillis();

// System.out.println("AI Response: " + response);
// System.out.println("Time taken: " + (end - start) + "ms");

// assertNotNull(response);
// assertTrue(response.contains("ghế") || response.contains("A1") ||
// response.contains("BOOKED") || response.contains("AVAILABLE"));
// }

// @Test
// public void testUserAgentAdvancedFlow() throws InterruptedException {
// System.out.println("--- Testing UserAgent Advanced Flow (Real API) ---");
// CineAgentProvider.CineAgent agent = CineAgentProvider.createUserAgent();
// Thread.sleep(15000);
// // Complex query: find a movie, then ask for showtimes
// agent.chat("adv-session", "Rạp có phim Spider-man không?");
// Thread.sleep(15000);
// String response = agent.chat("adv-session", "Vậy phim đó có lịch chiếu lúc
// nào?");

// System.out.println("AI Response: " + response);

// assertNotNull(response);
// assertTrue(response.contains("suất chiếu") || response.contains("lịch chiếu")
// || response.contains("ID"));
// }
// }
