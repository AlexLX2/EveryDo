package general;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Reminders {


    public static void showAlert() {
        Platform.runLater(() -> {
            ArrayList<Task> list = (ArrayList<Task>) getDueTasks();
            for (Task t : list) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(String.valueOf(t.getId()));
                alert.setContentText(t.getHeader());
                alert.show();
            }
        });
    }

    private static List<Task> getDueTasks() {
        List<Task> dueTasks = new ArrayList<>();
        try {
            dueTasks.add(DBWork.getInstance().getRecord(1));
            dueTasks.add(DBWork.getInstance().getRecord(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dueTasks;
    }
}

//        Date now = new Date();
//        Date nextRun = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
//        try {
//            nextRun = sdf.parse("22.07.2019 14:47");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(now);
//        System.out.println(nextRun);
//        System.out.println(now.getTime() - nextRun.getTime()<60000);
//        System.out.println(now.getTime() - nextRun.getTime());
////        if(now.getTime() - nextRun.getTime()<60000L)


