package service;

import dao.SeatDAO;
import dao.SeatTypeDAO;
import model.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SeatServiceTest {

    @Mock
    private SeatDAO seatDAO;

    @Mock
    private SeatTypeDAO seatTypeDAO;

    @Mock
    private Connection conn;

    @InjectMocks
    private SeatService seatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // SeatService initializes DAOs in constructor, but we'll inject mocking if possible 
        // Or if the constructor is using 'new', we might need to use reflection or change the class.
        // Let's check SeatService.java again.
    }

    @Test
    void testGenerateSeatsForHall_2x2() throws SQLException {
        when(seatTypeDAO.exists(any(), anyInt())).thenReturn(true);

        seatService.generateSeatsForHall(1, 2, 2, 1);

        // Verify it calls insert 4 times: A1, A2, B1, B2
        verify(seatDAO).insert(eq(1), eq("A1"), eq(0), eq(0), eq(1), eq(true));
        verify(seatDAO).insert(eq(1), eq("A2"), eq(0), eq(1), eq(1), eq(true));
        verify(seatDAO).insert(eq(1), eq("B1"), eq(1), eq(0), eq(1), eq(true));
        verify(seatDAO).insert(eq(1), eq("B2"), eq(1), eq(1), eq(1), eq(true));
    }

    @Test
    void testCreateSeat_InvalidType() throws SQLException {
        when(seatTypeDAO.exists(any(), eq(999))).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            seatService.createSeat(1, "A1", 0, 0, 999, true);
        });

        assertTrue(exception.getMessage().contains("Loại ghế không tồn tại"));
    }

    @Test
    void testUpdateSeatTypeBulk_EmptyList() {
        List<Integer> seatIds = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> {
            seatService.updateSeatTypeBulk(seatIds, 1);
        });
    }
}
