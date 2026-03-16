package ai.skills.admin;

import dao.MovieDAO;
import dao.DBConnect;
import model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MarketingBotSkillsTest {

    private MarketingBotSkills skills;

    @Mock
    private MovieDAO movieDAO;

    @Mock
    private Connection mockConnection;

    private MockedStatic<DBConnect> mockedDBConnect;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        skills = new MarketingBotSkills(movieDAO);
        mockedDBConnect = mockStatic(DBConnect.class);
    }

    @AfterEach
    public void tearDown() {
        mockedDBConnect.close();
    }

    @Test
    public void testGetMovieDetailsForMarketing() throws Exception {
        mockedDBConnect.when(DBConnect::getConnection).thenReturn(mockConnection);
        Movie movie = new Movie();
        movie.setTitle("Marketing Movie");
        movie.setAgeRating("PG-13");
        movie.setDescription("Cool movie");
        
        when(movieDAO.findById(mockConnection, 1)).thenReturn(movie);

        String result = skills.getMovieDetailsForMarketing(1);
        
        assertTrue(result.contains("Marketing Movie"));
        assertTrue(result.contains("PG-13"));
    }

    @Test
    public void testGetMoviesForMarketing() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Generic Movie");
        movie.setMovieId(10);
        
        when(movieDAO.getAllMovies()).thenReturn(Collections.singletonList(movie));

        String result = skills.getMoviesForMarketing();
        
        assertTrue(result.contains("Generic Movie"));
        assertTrue(result.contains("ID: 10"));
    }
}
