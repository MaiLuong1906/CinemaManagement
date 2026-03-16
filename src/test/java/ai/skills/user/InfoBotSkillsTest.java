package ai.skills.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import dao.MovieDAO;
import dao.ShowtimeDAO;
import dao.ProductDAO;
import model.Movie;
import model.Showtime;
import model.Product;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for InfoBotSkills using Mockito to avoid real database connections.
 */
public class InfoBotSkillsTest {

    private InfoBotSkills infoBotSkills;

    @Mock
    private MovieDAO movieDAO;
    @Mock
    private ShowtimeDAO showtimeDAO;
    @Mock
    private ProductDAO productDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        infoBotSkills = new InfoBotSkills(movieDAO, showtimeDAO, productDAO);
    }

    @Test
    public void testGetAllMovies() throws Exception {
        System.out.println("--- Testing getAllMovies (Mocked) ---");
        Movie m = new Movie();
        m.setTitle("Mock Movie");
        m.setMovieId(1);
        
        when(movieDAO.getAllMovies()).thenReturn(Arrays.asList(m));

        String result = infoBotSkills.getAllMovies();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Mock Movie"));
    }

    @Test
    public void testSearchMovies() throws Exception {
        System.out.println("--- Testing searchMovies (Query: 'Spider') (Mocked) ---");
        Movie m = new Movie();
        m.setTitle("Spider-man");
        m.setDescription("Web slinger");
        
        when(movieDAO.searchByTitle(any(), eq("Spider"))).thenReturn(Arrays.asList(m));

        String result = infoBotSkills.searchMovies("Spider");
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Spider-man"));
    }

    @Test
    public void testGetShowtimesForMovie() throws Exception {
        System.out.println("--- Testing getShowtimesForMovie (MovieID: 1) (Mocked) ---");
        Showtime s = new Showtime();
        s.setShowtimeId(101);
        s.setShowDate(java.time.LocalDate.now());
        
        when(showtimeDAO.findByMovie(any(), eq(1))).thenReturn(Arrays.asList(s));

        String result = infoBotSkills.getShowtimesForMovie(1);
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("101"));
    }

    @Test
    public void testGetComboProducts() throws Exception {
        System.out.println("--- Testing getComboProducts (Mocked) ---");
        Product p = new Product();
        p.setItemName("Mock Popcorn");
        p.setPrice(new java.math.BigDecimal("50000"));
        
        when(productDAO.findAll()).thenReturn(Arrays.asList(p));

        String result = infoBotSkills.getComboProducts();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Mock Popcorn"));
    }
}
