package dao;

import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieDAOTest extends BaseDAOTest {

    private MovieDAO movieDAO;

    @BeforeEach
    public void setup() throws Exception {
        movieDAO = new MovieDAO();
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM movie_genre_rel");
            stmt.execute("DELETE FROM showtimes");
            stmt.execute("DELETE FROM movies");
            stmt.execute("DELETE FROM movie_genres");
            
            stmt.execute("INSERT INTO movies (movie_id, title, duration, description, release_date, age_rating, poster_url) " +
                         "VALUES (1, 'Test Movie 1', 120, 'Desc 1', '2026-01-01', 'PG-13', 'url1')");
            stmt.execute("INSERT INTO movies (movie_id, title, duration, description, release_date, age_rating, poster_url) " +
                         "VALUES (2, 'Test Movie 2', 90, 'Desc 2', '2026-02-01', 'R', 'url2')");
                         
            stmt.execute("INSERT INTO movie_genres (genre_id, genre_name) VALUES (1, 'Action')");
            stmt.execute("INSERT INTO movie_genre_rel (movie_genre_id, movie_id, genre_id) VALUES (1, 1, 1)");

            stmt.execute("ALTER TABLE movies ALTER COLUMN movie_id RESTART WITH 10");
            stmt.execute("ALTER TABLE movie_genres ALTER COLUMN genre_id RESTART WITH 10");
            stmt.execute("ALTER TABLE movie_genre_rel ALTER COLUMN movie_genre_id RESTART WITH 10");
        }
    }

    @Test
    public void testFindById_Found() throws Exception {
        Movie m = movieDAO.findById(1);
        assertNotNull(m);
        assertEquals("Test Movie 1", m.getTitle());
    }

    @Test
    public void testFindById_NotFound() throws Exception {
        Movie m = movieDAO.findById(999);
        assertNull(m);
    }

    @Test
    public void testFindAll() throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            List<Movie> list = movieDAO.findAll(conn);
            assertEquals(2, list.size());
            assertEquals("Test Movie 2", list.get(0).getTitle()); // ordered by desc release_date
        }
    }
    
    @Test
    public void testGetAllMovies() {
        List<Movie> list = movieDAO.getAllMovies();
        assertEquals(2, list.size());
    }

    @Test
    public void testSearchByTitle() throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            List<Movie> list = movieDAO.searchByTitle(conn, "Movie 1");
            assertEquals(1, list.size());
            assertEquals(1, list.get(0).getMovieId());
        }
    }
    
    @Test
    public void testInsertAndReturnId_Delete() throws Exception {
        Movie m = new Movie();
        m.setTitle("New Insert Movie");
        m.setDuration(100);
        m.setDescription("Desc");
        m.setReleaseDate(LocalDate.now());
        m.setAgeRating("PG");
        m.setPosterUrl("url");
        
        try (Connection conn = DBConnect.getConnection()) {
            int newId = movieDAO.insertAndReturnId(conn, m);
            assertTrue(newId > 0);
            
            Movie found = movieDAO.findById(conn, newId);
            assertNotNull(found);
            assertEquals("New Insert Movie", found.getTitle());
            
            movieDAO.delete(conn, newId);
            assertNull(movieDAO.findById(newId));
        }
    }
    
    @Test
    public void testUpdate() throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            Movie m = movieDAO.findById(conn, 1);
            m.setTitle("Updated Title");
            
            movieDAO.update(conn, m);
            
            Movie updated = movieDAO.findById(1);
            assertEquals("Updated Title", updated.getTitle());
        }
    }
    
    @Test
    public void testGetMoviesByMultipleGenres() {
        List<Movie> list = movieDAO.getMoviesByMultipleGenres(new String[]{"Action"});
        assertEquals(1, list.size());
        assertEquals("Test Movie 1", list.get(0).getTitle());
        
        List<Movie> listEmpty = movieDAO.getMoviesByMultipleGenres(null);
        assertEquals(0, listEmpty.size());
    }

    @Test
    public void testSearchMovies() {
        List<Movie> list = movieDAO.getAllMovies();
        List<Movie> result = movieDAO.searchMovies(list, "Movie 1");
        assertEquals(1, result.size());
        
        List<Movie> resultEmpty = movieDAO.searchMovies(list, "Not Found Keyword");
        assertEquals(0, resultEmpty.size());
        
        List<Movie> resultNull = movieDAO.searchMovies(list, null);
        assertEquals(2, resultNull.size());
    }
    
    @Test
    public void testGetGenreIdByMovieId() {
        int relId = movieDAO.getGenreIdByMovieId(1);
        assertEquals(1, relId);
        
        int notFound = movieDAO.getGenreIdByMovieId(999);
        assertEquals(-1, notFound);
    }
}
