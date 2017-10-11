import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.ArrayList;


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

        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));


        grid.add(scenetitle, 0,0,2,1);

        TextField searchField = new TextField();
        searchField.setPrefWidth(400);
        grid.add(searchField,0,1);

        Button btn = new Button("Search");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 2, 1);

        Text returnedText = new Text();
        grid.add(returnedText, 0, 6);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                returnedText.setText("");
                if ((searchField.getText() != null && !searchField.getText().isEmpty())) {
                   String[] searchTerms = searchField.getText().split(" ");
                    DBOps search = new DBOps();
                    ArrayList<Recipe> recipes = search.ingredientMatch(searchTerms);
                    String returnedTitles = "Titles found: \n";
                    if (recipes.size() > 0) {
                        for (Recipe recipe : recipes) {
                            returnedTitles += " " + recipe.getTitle() + " \n";
                        }
                    } else {
                        returnedTitles += "No recipes found";
                    }
                    System.out.println(returnedTitles);

                    returnedText.setText(returnedTitles);


                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
