package ru.petrelevich;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SqlAppl {
    public static void main(String[] args) throws SQLException {
        System.out.println("application is starting...");
        new SqlAppl().start();
        System.out.println("application finished");
    }

    private void start() throws SQLException {
        try(Connection connection = makeConnection()) {
            System.out.println(connection.isValid(1));
        }
    }

    private Connection makeConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/postgres";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","password");
        props.setProperty("ssl","false");
        return DriverManager.getConnection(url, props);
    }
}
