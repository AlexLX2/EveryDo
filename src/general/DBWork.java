package general;

import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;

class DBWork {

    private static final String CONNSTR = "jdbc:sqlite:src/general/everydo.db";

    private static DBWork ourInstance = null;
    private Connection connection;

    private DBWork() throws SQLException {

        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CONNSTR);
        Task.lastID = getLastID();
        //System.out.println("База подключена");

    }

    static synchronized DBWork getInstance() throws SQLException {
        if (ourInstance == null)
            ourInstance = new DBWork();
        return ourInstance;
    }

    ArrayList<Task> getAllRecords() {
        ArrayList<Task> list = new ArrayList<>();
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

    Task getRecord(int id) {
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

    void insertRecord(Task task) {
        insertHeader(task.getId(), task.getHeader(), task.hasReminder());
        //insertBody(task.getId(), task.getBody());

    }

    private int getLastID() {
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

    private void insertHeader(int id, String header, boolean hasReminder) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO TASK_HEADERS(`id`, `header`, `hasreminder`) " +
                        "VALUES(?, ?, ?)")) {
            statement.setObject(1, id);
            statement.setObject(2, header);
            statement.setObject(3, hasReminder ? 1 : 0);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Task.lastID++;
    }

    private void insertBody(int id, String body) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO TASK_BODIES(`task_id`, `task_body`) " +
                        "VALUES(?, ?)")) {
            statement.setObject(1, id);
            statement.setObject(2, body);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String getBodyByID(int id) {
        String body = null;
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT TASK_BODY AS BODY FROM TASK_BODIES WHERE TASK_ID = " + id);
            while (resultSet.next()) {
                body = resultSet.getString("body");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return body;
    }

    boolean hasReminderByID(int id) {
        boolean hasreminder = false;
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT HASREMINDER AS hasreminder FROM TASK_HEADERS WHERE ID = " + id);
            while (resultSet.next()) {
                hasreminder = resultSet.getInt("hasreminder") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasreminder;
    }

    void deleteTaskByID(int id) {
        try {
            Statement statement = this.connection.createStatement();
            statement.addBatch("DELETE FROM TASK_HEADERS WHERE ID = " + id);
            statement.addBatch("DELETE FROM TASK_BODIES WHERE TASK_ID = " + id);
            statement.addBatch("DELETE FROM TASK_REMINDERS WHERE TASK_ID = " + id);
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateTaskByID(int id, String header, String body, boolean hasReminder) {
        try {
            Statement statement = this.connection.createStatement();
            statement.addBatch("UPDATE TASK_HEADERS SET HEADER = '" + header + "' , HASREMINDER = " + (hasReminder ? 1 : 0) + "  WHERE ID = " + id);
            statement.addBatch("REPLACE INTO TASK_BODIES (TASK_ID, TASK_BODY) VALUES (" + id + ", '" + body + "')");
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateTaskHeaderByID(int id, String header) {
        try {
            Statement statement = this.connection.createStatement();
            statement.execute("UPDATE TASK_HEADERS SET HEADER = '" + header + "' WHERE ID = " + id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
