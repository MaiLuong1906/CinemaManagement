package ai.skills;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dao.MovieDAO;
import dao.ShowtimeDAO;
import model.Movie;
import model.Showtime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class UserSkillsTest {

    @Mock
    private MovieDAO movieDAO;
    @Mock
    private ShowtimeDAO showtimeDAO;
    @Mock
    private Connection mockConnection;

    private UserSkills userSkills;

    @BeforeEach
    void setUp() {
        userSkills = new UserSkills(movieDAO, showtimeDAO, () -> mockConnection);
    }

    @Test
    void testSearchMovies() throws Exception {
        Movie m1 = new Movie(1, "Inception", 148, "Dream movie", LocalDate.now(), "P", null);
        when(movieDAO.searchByTitle(any(), eq("Inception"))).thenReturn(Arrays.asList(m1));

        String result = userSkills.searchMovies("Inception");

        assertTrue(result.contains("Inception"));
        assertTrue(result.contains("ID: 1"));
    }

    @Test
    void testSearchMoviesNotFound() throws Exception {
        when(movieDAO.searchByTitle(any(), eq("Unknown"))).thenReturn(Collections.emptyList());

        String result = userSkills.searchMovies("Unknown");

        assertTrue(result.contains("Không tìm thấy phim"));
    }

    @Test
    void testGetAllMovies() throws Exception {
        Movie m1 = new Movie(1, "Phim A", 120, "Desc", LocalDate.now(), "P", null);
        when(movieDAO.getAllMovies()).thenReturn(Arrays.asList(m1));

        String result = userSkills.getAllMovies();

        assertTrue(result.contains("Phim A"));
        assertTrue(result.contains("ID: 1"));
    }

    @Test
    void testGetShowtimesForMovie() throws Exception {
        Showtime s1 = new Showtime(1, 1, 1, LocalDate.of(2024, 3, 15), 2);
        when(showtimeDAO.findByMovie(any(), eq(1))).thenReturn(Arrays.asList(s1));

        String result = userSkills.getShowtimesForMovie(1);

        assertTrue(result.contains("2024-03-15"));
        assertTrue(result.contains("Slot ID: 2"));
    }

    @Test
    void testGetMovieDetails() throws Exception {
        Movie m1 = new Movie(1, "Avatar", 162, "Blue people", LocalDate.now(), "P", null);
        when(movieDAO.findById(any(), eq(1))).thenReturn(m1);

        String result = userSkills.getMovieDetails(1);

        assertTrue(result.contains("Avatar"));
        assertTrue(result.contains("162 phút"));
        assertTrue(result.contains("Blue people"));
    }
}
