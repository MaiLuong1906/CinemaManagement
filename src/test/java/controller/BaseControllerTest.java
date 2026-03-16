package controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

/**
 * Base class for testing Servlets using Mockito.
 */
public abstract class BaseControllerTest {

    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    @Mock
    protected HttpSession session;

    @Mock
    protected ServletConfig servletConfig;

    @Mock
    protected ServletContext servletContext;

    protected StringWriter responseWriter;

    @BeforeEach
    protected void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Setup session
        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);

        // Setup context
        when(servletConfig.getServletContext()).thenReturn(servletContext);

        // Setup writer to capture response output
        responseWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    /**
     * Helper to verify if the servlet redirected to a specific location.
     */
    protected void verifyRedirect(String location) throws Exception {
        verify(response).sendRedirect(location);
    }

    /**
     * Helper to verify if the request was dispatched (forwarded) to a specific path.
     */
    protected void verifyForward(String path) throws Exception {
        verify(request.getRequestDispatcher(path)).forward(request, response);
    }
}
