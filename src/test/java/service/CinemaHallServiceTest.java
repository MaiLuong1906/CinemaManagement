package service;

import dao.CinemaHallDAO;
import dao.SeatDAO;
import model.CinemaHall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CinemaHallServiceTest {

    private CinemaHallService service;

    @Mock
    private CinemaHallDAO hallDAO;

    @Mock
    private SeatDAO seatDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CinemaHallService(hallDAO, seatDAO);
    }

    @Test
    public void testCreateHallSuccess() throws SQLException {
        when(hallDAO.insert(any(CinemaHall.class))).thenReturn(1);
        int hallId = service.createHall("Hall 1", 10, 10);
        assertEquals(1, hallId);
        verify(hallDAO, times(1)).insert(any(CinemaHall.class));
    }

    @Test
    public void testCreateHallInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> service.createHall("", 10, 10));
    }

    @Test
    public void testCreateHallInvalidRows() {
        assertThrows(IllegalArgumentException.class, () -> service.createHall("Hall 1", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.createHall("Hall 1", 21, 10));
    }

    @Test
    public void testCreateHallInvalidCols() {
        assertThrows(IllegalArgumentException.class, () -> service.createHall("Hall 1", 10, 0));
        assertThrows(IllegalArgumentException.class, () -> service.createHall("Hall 1", 10, 31));
    }

    @Test
    public void testGetAllHalls() throws SQLException {
        CinemaHall hall = new CinemaHall(1, "Hall 1", 10, 10, true, java.time.LocalDate.now());
        List<CinemaHall> mockList = Arrays.asList(hall);
        when(hallDAO.getAllHalls()).thenReturn(mockList);
        
        List<CinemaHall> result = service.getAllHalls();
        assertEquals(1, result.size());
        assertEquals(hall, result.get(0));
    }

    @Test
    public void testGetHallById() throws SQLException {
        CinemaHall hall = new CinemaHall(1, "Hall 1", 10, 10, true, java.time.LocalDate.now());
        when(hallDAO.getHallById(1)).thenReturn(hall);
        assertEquals(hall, service.getHallById(1));
    }

    @Test
    public void testToggleHallStatus() throws SQLException {
        service.toggleHallStatus(1, true);
        verify(hallDAO, times(1)).updateStatus(1, true);
    }

    @Test
    public void testDeleteHall() throws SQLException {
        when(hallDAO.deleteHall(1)).thenReturn(true);
        boolean result = service.deleteHall(1);
        assertTrue(result);
        verify(seatDAO, times(1)).deleteByHall(1);
        verify(hallDAO, times(1)).deleteHall(1);
    }
}
