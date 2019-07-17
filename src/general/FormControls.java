package general;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FormControls {
    @FXML
    private ListView mainList;

    @FXML
    private Button btnAdd, btnDelete, btnEdit;

    @FXML
    private TextField txtAdd;


    public void initialize() {
        mainList.getItems().add("123");

        btnAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String text = txtAdd.getCharacters().toString();
                if (!text.equals(""))
                    mainList.getItems().add(text);
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
                    mainList.getItems().add(idx, txtAdd.getCharacters().toString());
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

}
