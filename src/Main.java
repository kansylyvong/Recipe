import java.util.ArrayList;

public class Main {
    public static void main(String [] args) {
        Scraper scraper = new Scraper();
       try {
          // Recipe recipe = scraper.scrape("");
          // System.out.println(recipe.getAuthorFirst());
           //System.out.println(recipe.getAuthorLast());
           DBOps saveToDB = new DBOps();
           String[] search = {"lemon"};
           saveToDB.ingredientMatch(search);



       } catch (Exception e) {
           System.err.println(e.getClass().getName() + ": " + e.getMessage());
       }
    }
}
