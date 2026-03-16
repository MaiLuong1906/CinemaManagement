package service;

import dao.TimeSlotDAO;
import dao.TimeSlotKpiDAO;
import model.TimeSlotKpiDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TimeSlotServiceTest {

    private TimeSlotService service;

    @Mock
    private TimeSlotDAO timeSlotDAO;

    @Mock
    private TimeSlotKpiDAO timeSlotKpiDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TimeSlotService(timeSlotDAO, timeSlotKpiDAO);
    }

    @Test
    public void testCountTimeSlotSuccess() throws SQLException {
        when(timeSlotDAO.countAllTimeSlots()).thenReturn(5);
        assertEquals(5, service.countTimeSlot());
    }

    @Test
    public void testCountTimeSlotException() throws SQLException {
        when(timeSlotDAO.countAllTimeSlots()).thenThrow(new SQLException("Error"));
        assertThrows(RuntimeException.class, () -> service.countTimeSlot());
    }

    @Test
    public void testGetTimeSlotKpiCurrentMonthSuccess() throws Exception {
        TimeSlotKpiDTO dto = new TimeSlotKpiDTO();
        List<TimeSlotKpiDTO> mockList = Arrays.asList(dto);
        when(timeSlotKpiDAO.getTimeSlotKpiCurrentMonth()).thenReturn(mockList);
        
        List<TimeSlotKpiDTO> result = service.getTimeSlotKpiCurrentMonth();
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    public void testGetTimeSlotKpiCurrentMonthException() throws Exception {
        when(timeSlotKpiDAO.getTimeSlotKpiCurrentMonth()).thenThrow(new Exception("Error"));
        assertThrows(RuntimeException.class, () -> service.getTimeSlotKpiCurrentMonth());
    }

    @Test
    public void testGetTotalTicketsBySlotCurrentMonthSuccess() throws Exception {
        int[] data = {1, 2};
        List<int[]> mockList = Arrays.asList(data);
        when(timeSlotKpiDAO.getTotalTicketsBySlotCurrentMonth()).thenReturn(mockList);
        
        List<int[]> result = service.getTotalTicketsBySlotCurrentMonth();
        assertEquals(1, result.size());
        assertEquals(data, result.get(0));
    }
}
