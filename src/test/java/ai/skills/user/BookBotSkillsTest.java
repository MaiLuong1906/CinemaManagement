package ai.skills.user;

import model.SeatSelectionDTO;
import model.BookingHistoryDTO;
import dao.InvoiceDAO;
import dao.SeatDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookBotSkills using Mockito to avoid real database connections.
 */
public class BookBotSkillsTest {

    @Mock
    private InvoiceDAO invoiceDAO;
    @Mock
    private SeatDAO seatDAO;
    @Mock
    private dao.TicketDetailDAO ticketDAO;
    @Mock
    private Connection mockConnection;

    private BookBotSkills bookBotSkills;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        // Create the real object with mocks
        BookBotSkills realSkills = new BookBotSkills(1, invoiceDAO, seatDAO, ticketDAO);
        // Create a spy to mock protected methods
        bookBotSkills = spy(realSkills);
        
        // Mock connection
        doReturn(mockConnection).when(bookBotSkills).getConnection();
        when(mockConnection.isClosed()).thenReturn(false);
    }

    @Test
    public void testGetMyBookingHistory() throws Exception {
        System.out.println("--- Testing getMyBookingHistory (Mocked) ---");
        BookingHistoryDTO dto = new BookingHistoryDTO();
        dto.setMovieTitle("Mock Movie");
        dto.setStatus("Paid");
        
        when(invoiceDAO.getBookingHistory(1)).thenReturn(Arrays.asList(dto));

        String result = bookBotSkills.getMyBookingHistory();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Mock Movie"));
    }

    @Test
    public void testGetSeatMap() throws Exception {
        System.out.println("--- Testing getSeatMap (Mocked) ---");
        // SeatSelectionDTO(int seatId, String seatCode, int rowIndex, int columnIndex, int seatTypeId, double price, String status)
        SeatSelectionDTO s = new SeatSelectionDTO(10, "A1", 0, 0, 1, 80000.0, "AVAILABLE");
        
        when(seatDAO.getSeatsByShowtime(1)).thenReturn(Arrays.asList(s));

        String result = bookBotSkills.getSeatMap(1);
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("A1"));
    }

    @Test
    public void testPrepareBooking() {
        System.out.println("--- Testing prepareBooking ---");
        String seatCodes = "A5, A6";
        int showtimeId = 1;
        double totalAmount = 160000;
        String movieName = "Godzilla x Kong";
        String result = bookBotSkills.prepareBooking(showtimeId, movieName, seatCodes, totalAmount);
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("BOOKING_CONFIRM"));
        assertTrue(result.contains("\"showtimeId\": 1"));
        assertTrue(result.contains("\"seats\": \"A5, A6\""));
    }

    @Test
    public void testConfirmBooking_Success() throws Exception {
        System.out.println("--- Testing confirmBooking (Mocked Success) ---");
        // SeatSelectionDTO(int seatId, String seatCode, int rowIndex, int columnIndex, int seatTypeId, double price, String status)
        SeatSelectionDTO s = new SeatSelectionDTO(10, "A1", 0, 0, 1, 80000.0, "AVAILABLE");
        
        when(seatDAO.getSeatsByShowtime(1)).thenReturn(Arrays.asList(s));
        when(invoiceDAO.insert(any(), any())).thenReturn(555);

        String result = bookBotSkills.confirmBooking(1, "A1", 80000);
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("BOOKING_SUCCESS"));
        assertTrue(result.contains("555"));
    }

    @Test
    public void testCancelInvoice() throws Exception {
        System.out.println("--- Testing cancelInvoice (Mocked) ---");
        String result = bookBotSkills.cancelInvoice(999);
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("thành công"));
        // Verification of invoiceDAO.updateStatus(999, "Canceled") could be added
    }
}
