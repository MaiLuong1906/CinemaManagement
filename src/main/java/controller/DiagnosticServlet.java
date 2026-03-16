package controller;

import dao.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.ConfigLoader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/diagnostic")
public class DiagnosticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        out.println("<html><head><title>Cinema Management Diagnostic</title>");
        out.println("<style>body{font-family:sans-serif;background:#121212;color:#eee;padding:20px;} .ok{color:#4CAF50;} .fail{color:#f44336;}</style>");
        out.println("</head><body>");
        out.println("<h1>System Diagnostic Report</h1>");
        
        // 1. Check Configuration
        out.println("<h2>1. Configuration Check</h2>");
        String dbUrl = ConfigLoader.get("db.url");
        String dbUser = ConfigLoader.get("db.username");
        out.println("<p>DB URL: " + (dbUrl != null ? "<b>" + dbUrl + "</b>" : "<span class='fail'>MISSING</span>") + "</p>");
        out.println("<p>DB User: " + (dbUser != null ? "<b>" + dbUser + "</b>" : "<span class='fail'>MISSING</span>") + "</p>");
        
        // 2. Database Connection Test
        out.println("<h2>2. Database Connection Test</h2>");
        out.println("<p>Testing connection (timeout in 5s)...</p>");
        out.flush(); // Send partial response to user so they see something
        
        long start = System.currentTimeMillis();
        try (Connection conn = DBConnect.getConnection()) {
            long end = System.currentTimeMillis();
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                out.println("<p class='ok'>CONNECTED SUCCESSFULLY! (" + (end - start) + "ms)</p>");
                out.println("<p>Database: " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion() + "</p>");
            } else {
                out.println("<p class='fail'>FAILED TO CONNECT (Returned Null)</p>");
            }
        } catch (Exception e) {
            out.println("<p class='fail'>EXCEPTION: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
        
        // 3. File System Check
        out.println("<h2>3. I/O Check</h2>");
        String imgDir = "C:/imgForCinema";
        java.io.File file = new java.io.File(imgDir);
        out.println("<ul>");
        out.println("<li><strong>Movie Directory (" + imgDir + "):</strong> " + (file.exists() ? "<span class='ok'>EXISTS</span>" : "<span class='fail'>NOT FOUND</span>") + "</li>");
        out.println("</ul>");

        out.println("<h2>4. Database Status (Critical Tables)</h2>");
        out.println("<table border='1' cellpadding='5' style='border-collapse: collapse;'>");
        out.println("<tr><th>Table / Entity</th><th>Status</th><th>Count / Info</th></tr>");

        checkTable(out, "Movies", "SELECT COUNT(*) FROM movies");
        checkTable(out, "Accounts", "SELECT COUNT(*) FROM accounts");
        checkTable(out, "Seats", "SELECT COUNT(*) FROM seats");
        checkTable(out, "Showtimes", "SELECT COUNT(*) FROM showtimes");
        checkTable(out, "Halls", "SELECT COUNT(*) FROM cinema_halls");
        checkTable(out, "Chat Messages", "SELECT COUNT(*) FROM chat_messages");

        out.println("</table>");

        out.println("<h2>5. Latest Logs (If any)</h2>");
        out.println("<p>Checking HomeServlet reaching... (See Console Output)</p>");
        
        out.println("<hr><p><a href='" + req.getContextPath() + "/home' style='color:#667eea'>Go to Home Page</a></p>");
        out.println("<br/><a href='home?debug=true'>Go to Home (Debug Mode)</a>");
        out.println("<br/><a href='home'>Go to Site Home</a>");

        out.println("</body></html>");
    }

    private void checkTable(PrintWriter out, String name, String sql) {
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                out.println("<tr><td>" + name + "</td><td style='color:green'>ACCESSIBLE</td><td>" + rs.getInt(1) + " records</td></tr>");
            }
        } catch (Exception e) {
            out.println("<tr><td>" + name + "</td><td style='color:red'>ERROR</td><td>" + e.getMessage() + "</td></tr>");
        }
    }
}
