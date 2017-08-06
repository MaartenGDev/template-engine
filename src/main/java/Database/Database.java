package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String uri;
    private final String username;
    private final String password;
    private Connection connection;
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    public Database(String uri, String username, String password) {
        this.uri = uri;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        if(this.connection != null && !this.connection.isClosed() &&  this.connection.isValid(5)) return this.connection;

        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(uri, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return this.connection;
    }

    public void close(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = null;
    }
}
