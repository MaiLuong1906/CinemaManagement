package controller;

import dao.*;
import model.Movie;
import model.MovieDetailDTO;
import model.UserDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MovieServletTest extends BaseControllerTest {

    private MovieServlet servlet;

    @Mock
    private MovieDAO movieDAO;

    @Mock
    private MovieDetailDAO movieDetailDAO;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servlet = new MovieServlet();
        
        // Inject mocks using reflection
        injectField("movieDAO", movieDAO);
        injectField("movieDetailDAO", movieDetailDAO);

        // Setup common request dispatcher mock
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = MovieServlet.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(servlet, value);
    }

    @Test
    public void testListMovies() throws Exception {
        // Setup
        when(request.getParameter("action")).thenReturn("list");
        List<Movie> movies = Collections.singletonList(new Movie());
        when(movieDAO.getAllMovies()).thenReturn(movies);

        // Execute
        servlet.handleRequest(request, response);

        // Verify
        verify(request).setAttribute("movies", movies);
        verify(request).setAttribute(eq("totalMovies"), anyInt());
        verifyForward("/views/user/movies.jsp");
    }

    @Test
    public void testAdminListMoviesAccessDenied() throws Exception {
        // Setup - No user in session
        when(request.getParameter("action")).thenReturn("admin-list");
        when(session.getAttribute("user")).thenReturn(null);

        // Execute
        servlet.handleRequest(request, response);

        // Verify SC_FORBIDDEN
        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    }

    @Test
    public void testAdminListMoviesSuccess() throws Exception {
        // Setup - Admin user in session
        when(request.getParameter("action")).thenReturn("admin-list");
        UserDTO admin = new UserDTO();
        admin.setRoleId("Admin");
        when(session.getAttribute("user")).thenReturn(admin);
        
        List<MovieDetailDTO> details = Collections.singletonList(new MovieDetailDTO());
        when(movieDetailDAO.getAllMovieDetails()).thenReturn(details);

        // Execute
        servlet.handleRequest(request, response);

        // Verify
        verify(request).setAttribute("movieDetails", details);
        verifyForward("/views/admin/movies/list.jsp");
    }

    @Test
    public void testAdminListMoviesSearch() throws Exception {
        // Setup - Admin user in session
        when(request.getParameter("action")).thenReturn("admin-list");
        when(request.getParameter("search")).thenReturn("Inception");
        
        UserDTO admin = new UserDTO();
        admin.setRoleId("Admin");
        when(session.getAttribute("user")).thenReturn(admin);
        
        List<MovieDetailDTO> details = Collections.singletonList(new MovieDetailDTO());
        when(movieDetailDAO.searchMovieDetails("Inception")).thenReturn(details);

        // Execute
        servlet.handleRequest(request, response);

        // Verify
        verify(request).setAttribute("movieDetails", details);
        verify(request).setAttribute("searchKeyword", "Inception");
        verifyForward("/views/admin/movies/list.jsp");
    }
}
