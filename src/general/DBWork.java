package general;

import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBWork {

    private static final String CONNSTR = "jdbc:sqlite:src/general/everydo.db";

    private static DBWork ourInstance = null;
    private Connection connection;

    private DBWork() throws SQLException {

        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CONNSTR);
        Task.lastID = getLastID();
        //System.out.println("База подключена");

    }

    public static synchronized DBWork getInstance() throws SQLException {
        if (ourInstance == null)
            ourInstance = new DBWork();
        return ourInstance;
    }

    public List<Task> getAllRecords() {
        List<Task> list = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TASK_HEADERS");
            while (resultSet.next()) {
                list.add(new Task(resultSet.getInt("id"), resultSet.getString("header")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Task getRecord(int id) {
        String header = null;
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TASK_HEADERS WHERE ID = " + id);
            while (resultSet.next()) {
                header = resultSet.getString("header");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Task(id, header);
    }

    public void insertRecord(String header) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO TASK_HEADERS(`id`, `header`) " +
                        "VALUES(?, ?)")) {
            statement.setObject(1, ++Task.lastID);
            statement.setObject(2, header);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getLastID() {
        int lastID = 0;
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(ID) AS ID FROM TASK_HEADERS");
            while (resultSet.next()) {
                lastID = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastID;
    }

}
