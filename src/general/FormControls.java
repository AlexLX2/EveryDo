package general;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import jfxtras.scene.control.CalendarPicker;

import java.sql.SQLException;
import java.util.ArrayList;

public class FormControls {
    @FXML
    private TableView mainList;

    @FXML
    private Button btnAdd, btnDelete, btnEdit;

    @FXML
    private TextField txtAdd;

    @FXML
    private TextArea txtDetails;

    @FXML
    private SplitPane detailPane;

    @FXML
    private CheckBox hasReminders;

    @FXML
    private CalendarPicker reminderDate;

    public void initialize() {

        fillList();


        reminderDate.withShowTime(true);


        detailPane.setStyle("-fx-box-border: transparent; -fx-padding: 0;");


        btnAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String header = txtAdd.getCharacters().toString();
                String body = txtDetails.getText();
                Boolean hasReminder = hasReminders.isSelected();
                if (!header.equals("")) {
                    Task task = new Task(Task.lastID + 1, header, body, hasReminder);
                    mainList.getItems().add(task);
                    writeTaskIntoDB(task);
                }
                txtAdd.setText("");
                txtDetails.setText("");
            }
        });


        btnDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Task selectedTask = (Task) mainList.getSelectionModel().getSelectedItem();
                int id = selectedTask.getId();
                int idx = mainList.getSelectionModel().getSelectedIndex();
                try {
                    DBWork.getInstance().deleteTaskByID(id);
                    mainList.getItems().remove(idx);
                    mainList.getSelectionModel().select(idx);
                    txtDetails.setText("");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Task selectedTask = (Task) mainList.getSelectionModel().getSelectedItem();
                    int id = selectedTask.getId();
                    String header = txtAdd.getCharacters().toString();
                    String body = txtDetails.getText();
                    boolean hasreminder = hasReminders.isSelected();
                    // String remindDate = reminderDate.getEditor().getText();
                    Task updatedTask = new Task(id, header, body, hasreminder);
                    DBWork.getInstance().updateTaskByID(id, header, body, hasreminder);
                    int idx = mainList.getSelectionModel().getSelectedIndex();
                    mainList.getItems().remove(idx);
                    mainList.getItems().add(idx, updatedTask);
                    mainList.getSelectionModel().select(idx);
                } catch (IndexOutOfBoundsException e) {
                    //e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        mainList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String header = null;
                String body = null;
                boolean hasReminder = false;
                try {
                    Task selectedTask = (Task) mainList.getSelectionModel().getSelectedItem();
                    header = selectedTask.getHeader();
                    body = DBWork.getInstance().getBodyByID(selectedTask.getId());
                    hasReminder = DBWork.getInstance().hasReminderByID(selectedTask.getId());
                } catch (NullPointerException e) {

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                txtAdd.setText(header);
                txtDetails.setText(body);
                hasReminders.setSelected(hasReminder);

            }
        });


    }

    public void fillList() {
        try {
            ArrayList<Task> list = (ArrayList<Task>) DBWork.getInstance().getAllRecords();

            //mainList = new TableView();
            TableColumn<String, Task> idcol = new TableColumn<>("ID");
            TableColumn<String, Task> headercol = new TableColumn<>("Header");

            idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            headercol.setCellValueFactory(new PropertyValueFactory<>("header"));

            mainList.getColumns().add(idcol);
            mainList.getColumns().add(headercol);

            for (Task t : list) {
                mainList.getItems().add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeTaskIntoDB(Task task) {
        try {
            DBWork.getInstance().insertRecord(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
