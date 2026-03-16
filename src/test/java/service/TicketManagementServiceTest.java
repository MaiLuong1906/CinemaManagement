package service;

import dao.TicketSoldBySlot_ViewDAO;
import dao.Ticket_Movie_ViewDAO;
import dao.TicketsSoldDAO;
import model.Movie_Ticket_ViewDTO;
import model.TicketSoldBySlot_ViewDTO;
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

public class TicketManagementServiceTest {

    private TicketManagementService service;

    @Mock
    private TicketsSoldDAO ticketsSoldDAO;

    @Mock
    private Ticket_Movie_ViewDAO ticketMovieViewDAO;

    @Mock
    private TicketSoldBySlot_ViewDAO bySlotViewDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TicketManagementService(ticketsSoldDAO, ticketMovieViewDAO, bySlotViewDAO);
    }

    @Test
    public void testGetMothlyTicketsSold() throws SQLException {
        when(ticketsSoldDAO.countSoldTicketsThisMonth()).thenReturn(500);
        assertEquals(500, service.getMothlyTicketsSold());
    }

    @Test
    public void testGetDailyTicketsSold() throws SQLException {
        when(ticketsSoldDAO.countSoldTicketsToday()).thenReturn(20);
        assertEquals(20, service.getDailyTicketsSold());
    }

    @Test
    public void testGetYearlyTicketsSold() throws SQLException {
        when(ticketsSoldDAO.countSoldTicketsThisYear()).thenReturn(6000);
        assertEquals(6000, service.getYearlyTicketsSold());
    }

    @Test
    public void testReturnNumberPageSuccess() throws SQLException {
        when(ticketMovieViewDAO.getTotalPages()).thenReturn(10);
        assertEquals(10, service.returnNumberPage());
    }

    @Test
    public void testReturnNumberPageException() throws SQLException {
        when(ticketMovieViewDAO.getTotalPages()).thenThrow(new SQLException("Error"));
        assertThrows(RuntimeException.class, () -> service.returnNumberPage());
    }

    @Test
    public void testGetAllOfPageNumberSuccess() throws SQLException {
        Movie_Ticket_ViewDTO dto = new Movie_Ticket_ViewDTO();
        List<Movie_Ticket_ViewDTO> mockList = Arrays.asList(dto);
        when(ticketMovieViewDAO.getByPage(1)).thenReturn(mockList);
        
        List<Movie_Ticket_ViewDTO> result = service.getAllOfPageNumber(1);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    public void testGetTicketSoldBySlotCurrentMonthSuccess() throws Exception {
        TicketSoldBySlot_ViewDTO dto = new TicketSoldBySlot_ViewDTO();
        List<TicketSoldBySlot_ViewDTO> mockList = Arrays.asList(dto);
        when(bySlotViewDAO.getAll()).thenReturn(mockList);
        
        List<TicketSoldBySlot_ViewDTO> result = service.getTicketSoldBySlotCurrentMonth();
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    public void testGetAllMovieStatsSuccess() throws SQLException {
        Movie_Ticket_ViewDTO dto = new Movie_Ticket_ViewDTO();
        List<Movie_Ticket_ViewDTO> mockList = Arrays.asList(dto);
        when(ticketMovieViewDAO.getAll()).thenReturn(mockList);
        
        List<Movie_Ticket_ViewDTO> result = service.getAllMovieStats();
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }
}
