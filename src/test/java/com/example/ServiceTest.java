package com.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.rowset.CachedRowSet;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {


    @Mock
    private Socket aSocket;
    @InjectMocks
    private Service service;
    @BeforeAll
    public void setUp() throws Exception {
        H2Setup h2Setup = new H2Setup();
        h2Setup.initialise();
    }
    @Test
    public void testAttendRequest(){
        Boolean aBoolean = service.attendRequest();
        assertEquals(aBoolean, Boolean.TRUE);

    }

//    @Test
//    public void testAttendInstructions() throws Exception {
//        ResultSet resultSet = service.attendInstructions();
//        int linesCount = 0;
//        while (resultSet.next()){
//            linesCount++;
//        }
//        assertNotNull(resultSet, "The instructions fetched is NULL");
//        assertTrue(linesCount > 0, "No data has been retrieved");
//    }

//    @Test
//    public void testAttendIngredients() throws Exception {
//        ResultSet resultSet = service.attendIngredients();
//        int linesCount = 0;
//        while (resultSet.next()){
//            linesCount++;
//        }
//        assertNotNull(resultSet, "The ingredients fetched is NULL");
//        assertTrue(linesCount > 0, "No data has been retrieved");
//    }

    @Test
    public void testH2Connection() throws Exception {
        String jdbcURL = "jdbc:h2:mem:receipe";
        Connection connection = DriverManager.getConnection(jdbcURL);
        Statement statement = connection.createStatement();
        String myQuery3 = "SELECT * FROM recipes WHERE recipe_id = 0;";
        ResultSet resultSet = statement.executeQuery(myQuery3);
        //To test our connection, we query the database and ensure it returns the right thing

        assertTrue(resultSet.next(), "The table has no rows");
        assertEquals("Eggs On Toast", resultSet.getString("recipe_name"), "The recipe name returned is wrong");
    }
}
