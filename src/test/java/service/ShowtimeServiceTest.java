package service;

import dao.ShowtimeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import dao.BaseDAOTest;

public class ShowtimeServiceTest extends BaseDAOTest {

    private ShowtimeService service;

    @Mock
    private ShowtimeDAO showtimeDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ShowtimeService(showtimeDAO);
    }

    @Test
    public void testDeleteShowtimeSuccess() throws Exception {
        when(showtimeDAO.delete(any(), eq(1))).thenReturn(true);
        service.deleteShowtime(1);
        verify(showtimeDAO, times(1)).delete(any(Connection.class), eq(1));
    }

    @Test
    public void testDeleteShowtimeNotFound() throws Exception {
        when(showtimeDAO.delete(any(), eq(999))).thenReturn(false);
        Exception exception = assertThrows(Exception.class, () -> service.deleteShowtime(999));
        assertEquals("Suất chiếu không tồn tại!", exception.getMessage());
    }
}
