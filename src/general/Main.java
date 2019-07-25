package general;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Main extends Application {

    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getResource("/general/mainWindows.fxml"));

        primaryStage.setTitle("EveryDo");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        //  startScheduleTask();
    }


    public void startScheduleTask() {
        /**
         * not using the taskHandle returned here, but it can be used to cancel
         * the task, or check if it done (for recurring tasks, that not
         * going to be very useful)
         */
        final ScheduledFuture<?> taskHandle = scheduler.scheduleAtFixedRate(
                new Runnable() {
                    public void run() {
                        try {
                            Reminders.showAlert();
                        } catch (Exception ex) {
                            ex.printStackTrace(); //or loggger would be better
                        }
                    }
                }, 0, 1, TimeUnit.MINUTES);
    }
}
