package service;

import dao.MovieDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    private MovieService service;

    @Mock
    private MovieDAO movieDao;

    @Mock
    private Connection conn;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new MovieService(movieDao);
    }

    @Test
    public void testDeleteMovieSuccess() throws Exception {
        doNothing().when(movieDao).delete(eq(conn), eq(1));
        boolean result = service.DeleteMovie(conn, 1);
        assertTrue(result);
        verify(movieDao, times(1)).delete(eq(conn), eq(1));
    }

    @Test
    public void testDeleteMovieNotFound() throws Exception {
        doThrow(new SQLException("NotFound")).when(movieDao).delete(eq(conn), eq(999));
        assertThrows(Exception.class, () -> service.DeleteMovie(conn, 999));
    }
}
