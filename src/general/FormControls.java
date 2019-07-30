package general;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class FormControls {
    @FXML
    private TableView<Task> mainList;

    @FXML
    private Button btnAdd, btnDelete;

    @FXML
    private TextField txtAdd;

    public void initialize() {

        fillList();
        mainList.requestFocus();
        mainList.setEditable(true);


        btnAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addNewTask());

        txtAdd.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                addNewTask();
        });

        btnDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteTask());

        mainList.setOnMouseClicked(mouseEvent -> editTask());
    }

    private void editTask() {

        String header = null;
        try {
            Task selectedTask = mainList.getSelectionModel().getSelectedItem();
            header = selectedTask.getHeader();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        txtAdd.setText(header);

    }

    private void deleteTask() {
        Task selectedTask = mainList.getSelectionModel().getSelectedItem();
        int id = selectedTask.getId();
        int idx = mainList.getSelectionModel().getSelectedIndex();
        try {
            DBWork.getInstance().deleteTaskByID(id);
            mainList.getItems().remove(idx);
            mainList.getSelectionModel().select(idx);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addNewTask() {
        String header = txtAdd.getCharacters().toString();

        if (!header.equals("")) {
            Task task = new Task(Task.lastID + 1, header, "", false);
            mainList.getItems().add(task);
            writeTaskIntoDB(task);
        }
        txtAdd.setText("");
    }

    private void fillList() {
        try {
            ArrayList<Task> list = DBWork.getInstance().getAllRecords();

            TableColumn<Task, String> idcol = new TableColumn<>("ID");
            idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            idcol.prefWidthProperty().bind(mainList.widthProperty().multiply(0.25)); // w * 1/4
            idcol.setVisible(false);


            TableColumn<Task, String> headercol = new TableColumn<>();
            headercol.setCellValueFactory(new PropertyValueFactory<>("header"));
            headercol.setCellFactory(TextFieldTableCell.forTableColumn());
            headercol.setOnEditCommit(t -> {
                int id = t.getTablePosition().getRow();
                Task task = t.getTableView().getItems().get(id);
                try {
                    DBWork.getInstance().updateTaskHeaderByID(task.getId(), t.getNewValue());
                    mainList.setItems(FXCollections.observableList(DBWork.getInstance().getAllRecords()));

                    Platform.runLater(() -> {
                        mainList.requestFocus();
                        mainList.getSelectionModel().select(id);
                        mainList.getFocusModel().focus(id, headercol);
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });
            headercol.prefWidthProperty().bind(mainList.widthProperty().multiply(1)); // w * 1/2
            headercol.setStyle("-fx-font-size :22px");
            headercol.setEditable(true);

            mainList.getColumns().add(idcol);
            mainList.getColumns().add(headercol);


            for (Task t : list) {
                mainList.getItems().add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void writeTaskIntoDB(Task task) {
        try {
            DBWork.getInstance().insertRecord(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
