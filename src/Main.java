import java.util.ArrayList;
import java.util.*;

public class Main {
    public static void main (String [] args) {
        ArrayList<String> ingList = new ArrayList<String>();
        ingList.add("cake_ingredient1");
        ingList.add("cake_ingredient2");
        ingList.add("cake_ingredient3");
        ingList.add("cake_ingredient4");
        ArrayList<String> stepList = new ArrayList<String>();
        stepList.add("cake_step1");
        stepList.add("cake_rstep2");
        stepList.add("cake_rstep3");
        ArrayList<String> catList = new ArrayList<String>();
        catList.add("rcat1");
        catList.add("rcat2");
        catList.add("rcat3");
        catList.add("rcat4");
        Recipe clientRecipe = new Recipe("thomas", "keller", "Chocolate cake", ingList, stepList, "hard", 20, 15, 70, 4 /*, catList */);

        /*String author = clientRecipe.getAuthor();
        String title = clientRecipe.getTitle();
        ArrayList<String> ingredients = clientRecipe.getIngredients();

        System.out.println("Author name: " + author);
        System.out.println("Title: " + title);
        System.out.print("Ingredients: ");
        for (String item: ingredients) {
            System.out.print(item + " ");
        }
        System.out.println("");
        ArrayList<String> newIng = new ArrayList<>();
        newIng.add("new1"); newIng.add("new2");
        clientRecipe.addIngredients(newIng);
        System.out.println("Updated Ingredients List: ");
        for (String ingredient: ingredients) {
            System.out.print(ingredient + " ");
        } */
        DBOps sample = new DBOps();
        ArrayList<Recipe> recipes = sample.searchRecipes("1");
        HashMap<Integer, String> steps = new HashMap<>();
        HashMap<Integer, String> ingredients = new HashMap<>();
        for (Recipe recipe: recipes) {
            System.out.println("Recipe title: " + recipe.getTitle());
            steps = recipe.getReturnedSteps();
            ingredients = recipe.getReturnedIngredients();
            for (HashMap.Entry<Integer, String> step: steps.entrySet()) {
                System.out.print("Step: ");
                System.out.print(step.getValue() + " ");
            }
            System.out.println(" ");
            System.out.print("Ingredient: ");
            for (HashMap.Entry<Integer, String> ingredient: ingredients.entrySet()) {
                System.out.print(ingredient.getValue() + " ");
            }
            System.out.println(" ");
            System.out.println(" ");
       }
     //String[] ingredientList = {"step1", "step2"};
     //sample.ingredientMatch(ingredientList);
    }
}
