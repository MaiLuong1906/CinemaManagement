package util;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieUtilsTest {

    @Mock
    private HttpServletRequest request;

    @Test
    public void testGetIntParameter_Valid() {
        when(request.getParameter("id")).thenReturn("123");
        assertEquals(123, MovieUtils.getIntParameter(request, "id"));
    }

    @Test
    public void testGetIntParameter_Invalid() {
        when(request.getParameter("id")).thenReturn("abc");
        assertThrows(IllegalArgumentException.class, () -> {
            MovieUtils.getIntParameter(request, "id");
        });
    }

    @Test
    public void testGetStringParameter_Valid() {
        when(request.getParameter("title")).thenReturn(" Inception ");
        assertEquals("Inception", MovieUtils.getStringParameter(request, "title"));
    }

    @Test
    public void testGetLocalDateParameter_Future() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        when(request.getParameter("date")).thenReturn(futureDate.toString());
        assertEquals(futureDate, MovieUtils.getLocalDateParameter(request, "date"));
    }

    @Test
    public void testGetLocalDateParameter_Past() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        when(request.getParameter("date")).thenReturn(pastDate.toString());
        assertThrows(IllegalArgumentException.class, () -> {
            MovieUtils.getLocalDateParameter(request, "date");
        });
    }
}
