package ai.skills.admin;

import dao.UserProfileDAO;
import dao.DBConnect;
import model.CinemaHall;
import model.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ModerateBotSkillsTest {

    private ModerateBotSkills skills;

    @Mock
    private UserProfileDAO userProfileDAO;

    @Mock
    private Connection mockConnection;

    private MockedStatic<DBConnect> mockedDBConnect;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        skills = new ModerateBotSkills(userProfileDAO);
        mockedDBConnect = mockStatic(DBConnect.class);
    }

    @AfterEach
    public void tearDown() {
        mockedDBConnect.close();
    }

    @Test
    public void testGetUserList() throws Exception {
        UserDTO user = new UserDTO();
        user.setFullName("John Doe");
        user.setEmail("john@example.com");
        user.setRoleId("User");
        
        when(userProfileDAO.getAllUsers()).thenReturn(Collections.singletonList(user));

        String result = skills.getUserList();
        
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("john@example.com"));
    }

    @Test
    public void testGetHallStatus() throws Exception {
        // Setup static mock for DBConnect
        mockedDBConnect.when(DBConnect::getConnection).thenReturn(mockConnection);
        
        java.sql.PreparedStatement mockPs = mock(java.sql.PreparedStatement.class);
        java.sql.ResultSet mockRs = mock(java.sql.ResultSet.class);
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false); // One row then end
        when(mockRs.getInt("hall_id")).thenReturn(1);
        when(mockRs.getString("hall_name")).thenReturn("Hall 1");
        when(mockRs.getInt("total_rows")).thenReturn(10);
        when(mockRs.getInt("total_cols")).thenReturn(12);
        when(mockRs.getBoolean("status")).thenReturn(true);
        when(mockRs.getDate("created_at")).thenReturn(java.sql.Date.valueOf(java.time.LocalDate.now()));

        String status = skills.getHallStatus();
        
        assertNotNull(status);
        assertTrue(status.contains("Hall 1"));
        assertTrue(status.contains("10x12"));
    }
}
