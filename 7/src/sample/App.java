package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Greditor");
        primaryStage.setScene(new Scene(root, 962, 695));
        primaryStage.setMaxHeight(732);
        primaryStage.setMaxWidth(978);
        primaryStage.setMinHeight(732);
        primaryStage.setMinWidth(978);
        primaryStage.show();
    }

}

