package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import javafx.stage.Stage;


public class View extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Search Recipes");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 25, 25));

        Scene scene = new Scene(grid, 700, 800);

        Text scenetitle = new Text("Search Recipes");
        //scenetitle.setAlignment(Pos.CENTER_RIGHT);
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));


        grid.add(scenetitle, 0,0,2,1);

        TextField searchField = new TextField();
        searchField.setPrefWidth(400);
        grid.add(searchField,1,1);


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
