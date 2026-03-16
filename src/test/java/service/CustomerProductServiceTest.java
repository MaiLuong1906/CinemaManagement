package service;

import dao.ProductDAO;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CustomerProductServiceTest {

    private CustomerProductService service;

    @Mock
    private ProductDAO productDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CustomerProductService(productDAO);
    }

    @Test
    public void testFindAll() throws SQLException {
        Product p = new Product();
        List<Product> mockList = Arrays.asList(p);
        when(productDAO.findAll()).thenReturn(mockList);
        
        List<Product> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(p, result.get(0));
    }
}
