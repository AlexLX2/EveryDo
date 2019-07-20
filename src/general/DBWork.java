package general;

import org.sqlite.JDBC;

import java.sql.*;

public class DBWork {

    private static final String CONNSTR = "jdbc:sqlite:src/general/everydo.db";

    private static DBWork ourInstance = null;
    private Connection connection;

    private DBWork() throws SQLException {

        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CONNSTR);
        System.out.println("База подключена");

    }

    public static synchronized DBWork getInstance() throws SQLException {
        if (ourInstance == null)
            ourInstance = new DBWork();
        return ourInstance;
    }

    public void getRecord() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TASK_HEADERS");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id"));
                System.out.println(resultSet.getString("header"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
