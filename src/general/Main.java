package general;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getResource("/general/mainWindows.fxml"));

        primaryStage.setTitle("EveryDo");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        DBWork.getInstance().getRecord();


    }
}
