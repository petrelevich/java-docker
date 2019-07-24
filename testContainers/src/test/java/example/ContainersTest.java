package example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class ContainersTest {
    private static final Logger logger = LoggerFactory.getLogger(ContainersTest.class);

    // will be started before and stopped after each test method
    @Container
    private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("testDataBase")
            .withUsername("owner")
            .withPassword("secret")
            .withClasspathResourceMapping("00_createTables.sql", "/docker-entrypoint-initdb.d/00_createTables.sql", BindMode.READ_ONLY)
            .withClasspathResourceMapping("01_insertData.sql", "/docker-entrypoint-initdb.d/01_insertData.sql", BindMode.READ_ONLY);

    @Test
    @DisplayName("Connection test")
    public void connectionTest() throws SQLException {
        try (Connection connection = makeConnection()) {
            boolean connectionStatus = connection.isValid(1);
            logger.info("connection validation:{}", connectionStatus);
            assertTrue(connectionStatus);
        }
    }

    @Test
    @DisplayName("Insert and Select test")
    public void insertSelectTest() throws SQLException {
        String userName = "Ivan";
        long expectedUserId = 3;
        try (Connection connection = makeConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("insert into account(user_name) values (?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, userName);
                int rows = ps.executeUpdate();
                assertEquals(1, rows, "Inserted rows");
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    long createdUserId = rs.getInt(1);
                    assertEquals(expectedUserId, createdUserId, "Expected new userId");
                }

            }

            try (PreparedStatement ps = connection.prepareStatement("select user_id, user_name from account where user_id = ?")) {
                ps.setLong(1, expectedUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long userIdFromDb = rs.getLong("user_id");
                        String userNameFromDb = rs.getString("user_name");
                        logger.info("selected userId:{}, userName:{}", userIdFromDb, userNameFromDb);
                        assertEquals(expectedUserId, userIdFromDb, "Expected selected userId");
                        assertEquals(userName, userNameFromDb, "Expected selected userName");
                    }
                }
            }
        }
    }

    private Connection makeConnection() throws SQLException {
        String url = postgresqlContainer.getJdbcUrl();
        Properties props = new Properties();
        props.setProperty("user", postgresqlContainer.getUsername());
        props.setProperty("password", postgresqlContainer.getPassword());
        props.setProperty("ssl", "false");
        return DriverManager.getConnection(url, props);
    }

}
