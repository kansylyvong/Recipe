import java.util.ArrayList;

public class Main {
    public static void main(String [] args) {
        Scraper scraper = new Scraper();
       try {
           Recipe recipe = scraper.scrape("http://www.foodandwine.com/recipes/ancho-scallion-roast-turkey-breast");
            System.out.println(recipe.getAuthorFirst());
            System.out.println(recipe.getAuthorLast());
           DBOps saveToDB = new DBOps();
           saveToDB.insertRecipe(recipe);


       } catch (Exception e) {
           System.err.println(e.getClass().getName() + ": " + e.getMessage());
       }
    }
}
