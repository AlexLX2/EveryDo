package general;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class FormControls {
    @FXML
    private TableView mainList;

    @FXML
    private Button btnAdd, btnDelete, btnEdit;

    @FXML
    private TextField txtAdd;


    public void initialize() {

        fillList();


        btnAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String text = txtAdd.getCharacters().toString();
                if (!text.equals(""))
                    mainList.getItems().add(new Task(Task.lastID + 1, text));
                writeTaskIntoDB(text);
                txtAdd.setText("");
            }
        });

        btnDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int idx = mainList.getSelectionModel().getSelectedIndex();
                mainList.getItems().remove(idx);
                mainList.getSelectionModel().select(idx);

            }
        });

        btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    int idx = mainList.getSelectionModel().getSelectedIndex();
                    //txtAdd.setText(mainList.getSelectionModel().getSelectedItem().toString());
                    mainList.getItems().remove(idx);
                    mainList.getItems().add(idx, new Task(idx, txtAdd.getCharacters().toString()));
                    mainList.getSelectionModel().select(idx);
                } catch (IndexOutOfBoundsException e) {
                    //e.printStackTrace();
                }
            }
        });

        mainList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String text = null;
                try {
                    text = mainList.getSelectionModel().getSelectedItem().toString();
                } catch (NullPointerException e) {

                }
                txtAdd.setText(text);
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

    public void writeTaskIntoDB(String header) {
        try {
            DBWork.getInstance().insertRecord(header);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
