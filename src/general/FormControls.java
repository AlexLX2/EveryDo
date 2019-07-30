package general;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.CalendarPicker;

import java.sql.SQLException;
import java.util.ArrayList;

public class FormControls {
    @FXML
    private TableView<Task> mainList;

    @FXML
    private VBox mainBox;

    @FXML
    private AnchorPane anchorMain;

    @FXML
    private AnchorPane anchorDetail;

    @FXML
    private SplitPane generalPane;

    @FXML
    private MenuItem testBtn;

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

    private boolean isDetailOpen = true;

    public void initialize() {

        showHideDetailPane();

        fillList();
        mainList.requestFocus();
        mainList.setEditable(true);
        reminderDate.withShowTime(true);
        reminderDate.setVisible(false);

        detailPane.setStyle("-fx-box-border: transparent; -fx-padding: 0;");
        detailPane.setDividerPosition(0, 0.85);
        detailPane.setDividerPosition(1, 0.90);
        detailPane.setDividerPosition(2, 0.95);


        hasReminders.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (hasReminders.isSelected()) {
                    reminderDate.setVisible(hasReminders.isSelected());
                    detailPane.setDividerPosition(0, 0.50);
                    detailPane.setDividerPosition(1, 0.90);
                    detailPane.setDividerPosition(2, 0.90);
                } else {
                    reminderDate.setVisible(hasReminders.isSelected());
                    detailPane.setDividerPosition(0, 0.85);
                    detailPane.setDividerPosition(1, 0.90);
                    detailPane.setDividerPosition(2, 0.90);
                }


            }
        });

        btnAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String header = txtAdd.getCharacters().toString();
                String body = txtDetails.getText();
                boolean hasReminder = hasReminders.isSelected();
                if (!header.equals("")) {
                    Task task = new Task(Task.lastID + 1, header, body, hasReminder);
                    //noinspection unchecked
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
                Task selectedTask = mainList.getSelectionModel().getSelectedItem();
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
                    Task selectedTask = mainList.getSelectionModel().getSelectedItem();
                    int id = selectedTask.getId();
                    String header = txtAdd.getCharacters().toString();
                    String body = txtDetails.getText();
                    boolean hasreminder = hasReminders.isSelected();
                    // String remindDate = reminderDate.getEditor().getText();
                    Task updatedTask = new Task(id, header, body, hasreminder);
                    DBWork.getInstance().updateTaskByID(id, header, body, hasreminder);
                    int idx = mainList.getSelectionModel().getSelectedIndex();
                    mainList.getItems().remove(idx);
                    //noinspection unchecked
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
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() >= 2) {
                    showHideDetailPane();
                } else {
                    String header = null;
                    String body = null;
                    boolean hasReminder = false;
                    try {
                        Task selectedTask = mainList.getSelectionModel().getSelectedItem();
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
            }
        });


        testBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showHideDetailPane();
            }
        });


    }


    private void showHideDetailPane() {
        if (isDetailOpen) {
            generalPane.setDividerPosition(0, 1);
            isDetailOpen = false;
        } else {
            generalPane.setDividerPosition(0, 0.5);
            isDetailOpen = true;
        }
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
