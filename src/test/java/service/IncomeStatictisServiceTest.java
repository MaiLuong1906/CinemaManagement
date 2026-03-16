package service;

import dao.InvoiceDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class IncomeStatictisServiceTest {

    private IncomeStatictisService service;

    @Mock
    private InvoiceDAO invoiceDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new IncomeStatictisService(invoiceDAO);
    }

    @Test
    public void testCalculateMonthlyRevenue() {
        when(invoiceDAO.calculateRevenue()).thenReturn(1000.0);
        assertEquals(1000.0, service.calculateMonthlyRevenue());
    }

    @Test
    public void testCalculateTicketRevenue() {
        when(invoiceDAO.getMonthlyTicketRevenue()).thenReturn(600.0);
        assertEquals(600.0, service.calculateTicketRevenue());
    }

    @Test
    public void testCalculateProductRevenue() {
        when(invoiceDAO.getMonthlyProductRevenue()).thenReturn(400.0);
        assertEquals(400.0, service.calculateProductRevenue());
    }

    @Test
    public void testCalculateTotalRevenue() {
        when(invoiceDAO.getMonthlyTicketRevenue()).thenReturn(600.0);
        when(invoiceDAO.getMonthlyProductRevenue()).thenReturn(400.0);
        assertEquals(1000.0, service.calculateTotalRevenue());
    }

    @Test
    public void testCalculatePercentTicket() {
        when(invoiceDAO.getMonthlyTicketRevenue()).thenReturn(600.0);
        when(invoiceDAO.getMonthlyProductRevenue()).thenReturn(400.0);
        assertEquals(0.6, service.calculatePercentTicket());
    }

    @Test
    public void testCalculatePercentProduct() {
        when(invoiceDAO.getMonthlyTicketRevenue()).thenReturn(600.0);
        when(invoiceDAO.getMonthlyProductRevenue()).thenReturn(400.0);
        assertEquals(0.4, service.calculatePercentProduct());
    }

    @Test
    public void testGetDaylyRevenue() {
        when(invoiceDAO.getDailyRevenue()).thenReturn(100.0);
        assertEquals(100.0, service.getDaylyRevenue());
    }

    @Test
    public void testGetYearlyRevenue() {
        when(invoiceDAO.getYearlyRevenue()).thenReturn(12000.0);
        assertEquals(12000.0, service.getYearlyRevenue());
    }
}
