package hospital_db;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.util.Scanner;


public class HospitalDB extends Application {
    private HospitalView hospitalView;
    private Connection connect;
    @Override
    public void start(Stage primaryStage) throws Exception {
        String url, username, password;
        url = username = password = "";
        try {
            File cfg = new File("settings.cfg");
            Scanner cfgScan = new Scanner(cfg);
            url = cfgScan.nextLine();
            username = cfgScan.nextLine();
            password = cfgScan.nextLine();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText("Не удалось открыть конфигурационный файл, или в нем присутствуют ошибки.");
            alert.setOnCloseRequest(e -> alert.close());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            System.exit(1);
        }
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

        primaryStage.setOnCloseRequest(e -> System.exit(0));
        try {
            connect = DriverManager.getConnection(url, username, password);
            VBox root = new VBox();
            primaryStage.setTitle("Дополнительные выплаты");
            primaryStage.setScene(new Scene(root, 1300, 500));
            hospitalView = new HospitalView(connect);
            root.getChildren().add(hospitalView);
            primaryStage.show();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText("Не удалось подключиться у базе данных. Причина неисправности может быть " +
                    "в некорректном файле конфигурации или отсутствии соединения с сетью.");
            alert.setOnCloseRequest(e -> alert.close());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
