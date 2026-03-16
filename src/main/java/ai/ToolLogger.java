package ai;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Utility to route background AI tool execution logs back to the active user's SSE connection.
 */
public class ToolLogger {
    
    // Maps a userId to their active SSE connection's log consumer
    private static final ConcurrentHashMap<Integer, Consumer<String>> activeConnections = new ConcurrentHashMap<>();

    /**
     * Registers a writer function for a specific user session.
     */
    public static void register(int userId, Consumer<String> writer) {
        activeConnections.put(userId, writer);
    }

    /**
     * Unregisters the writer when the SSE connection drops or finishes.
     */
    public static void unregister(int userId) {
        activeConnections.remove(userId);
    }

    /**
     * Broadcasts an execution event to the user's active connection.
     * The message should be a JSON payload, e.g., {"tool": "Tên thao tác..."}
     */
    public static void log(int userId, String jsonPayload) {
        Consumer<String> writer = activeConnections.get(userId);
        if (writer != null) {
            writer.accept("data: " + jsonPayload + "\n\n");
        }
    }
}
