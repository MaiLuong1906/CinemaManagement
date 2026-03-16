package service;

import dao.SeatFillRate_ViewDAO;
import model.SeatFillRate_ViewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class SeatFillRate_ViewServiceTest {

    private SeatFillRate_ViewService service;

    @Mock
    private SeatFillRate_ViewDAO dao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new SeatFillRate_ViewService(dao);
    }

    @Test
    public void testGetSeatFillRateCurrentMonth() {
        when(dao.getCinemaFillRateCurrentMonth()).thenReturn(0.75);
        assertEquals(0.75, service.getSeatFillRateCurrentMonth());
    }

    @Test
    public void testGetSeatFillRateForShowtimeCurrentMonthSuccess() throws Exception {
        SeatFillRate_ViewDTO dto = new SeatFillRate_ViewDTO();
        List<SeatFillRate_ViewDTO> mockList = Arrays.asList(dto);
        when(dao.getSeatFillRateByTimeSlotCurrentMonth()).thenReturn(mockList);
        
        List<SeatFillRate_ViewDTO> result = service.getSeatFillRateForShowtimeCurrentMonth();
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    public void testGetSeatFillRateForShowtimeCurrentMonthException() throws Exception {
        when(dao.getSeatFillRateByTimeSlotCurrentMonth()).thenThrow(new RuntimeException("SQL Error"));
        assertThrows(RuntimeException.class, () -> service.getSeatFillRateForShowtimeCurrentMonth());
    }

    @Test
    public void testGetAllCurrentMonthSuccess() throws Exception {
        SeatFillRate_ViewDTO dto = new SeatFillRate_ViewDTO();
        List<SeatFillRate_ViewDTO> mockList = Arrays.asList(dto);
        when(dao.getAllSeatCoverageCurrentMonth()).thenReturn(mockList);
        
        List<SeatFillRate_ViewDTO> result = service.getAllCurrentMonth();
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    public void testGetAllCurrentMonthException() throws Exception {
        when(dao.getAllSeatCoverageCurrentMonth()).thenThrow(new RuntimeException("SQL Error"));
        assertThrows(RuntimeException.class, () -> service.getAllCurrentMonth());
    }
}
