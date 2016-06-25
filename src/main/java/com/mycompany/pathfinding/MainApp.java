package com.mycompany.pathfinding;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox mainPanel = new VBox();
        
        GameMainInfrastructure gameMainInfrastructure = new GameMainInfrastructure(stage, mainPanel);
        gameMainInfrastructure.beginGameLoop();

        Scene scene = new Scene(mainPanel);
        scene.getStylesheets().add("/styles/Styles.css");
     
        stage.setTitle("Robot Liberation Day");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
