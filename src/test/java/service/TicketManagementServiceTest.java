package service;

import dao.TicketsSoldDAO;
import dao.Ticket_Movie_ViewDAO;
import dao.TicketSoldBySlot_ViewDAO;
import model.Movie_Ticket_ViewDTO;
import model.TicketSoldBySlot_ViewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TicketManagementServiceTest {

    private TicketManagementService service;

    @Mock
    private TicketsSoldDAO ticketsSoldDAO;

    @Mock
    private Ticket_Movie_ViewDAO ticket_Movie_ViewDAO;

    @Mock
    private TicketSoldBySlot_ViewDAO bySlot_ViewDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TicketManagementService(ticketsSoldDAO, ticket_Movie_ViewDAO, bySlot_ViewDAO);
    }

    @Test
    public void testGetMothlyTicketsSold() throws SQLException {
        when(ticketsSoldDAO.countSoldTicketsThisMonth()).thenReturn(150);
        assertEquals(150, service.getMothlyTicketsSold());
    }

    @Test
    public void testReturnNumberPageSuccess() throws SQLException {
        when(ticket_Movie_ViewDAO.getTotalPages()).thenReturn(5);
        assertEquals(5, service.returnNumberPage());
    }

    @Test
    public void testReturnNumberPageError() throws SQLException {
        when(ticket_Movie_ViewDAO.getTotalPages()).thenThrow(new SQLException("DB Error"));
        assertThrows(RuntimeException.class, () -> service.returnNumberPage());
    }

    @Test
    public void testGetAllOfPageNumber() throws SQLException {
        List<Movie_Ticket_ViewDTO> mockList = Collections.singletonList(new Movie_Ticket_ViewDTO());
        when(ticket_Movie_ViewDAO.getByPage(1)).thenReturn(mockList);
        assertEquals(mockList, service.getAllOfPageNumber(1));
    }
}
