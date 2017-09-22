import java.sql.*;
import java.util.ArrayList;

public class DBOps{
    public void insert(Recipe recipe, Connection c, Integer id) {
        //insert the author first
        try {
            int authId;
            if (id == 0) {
                String prepAuth = "insert into authors (first_name, last_name) values(?, ?);";
                PreparedStatement insertAuth = c.prepareStatement(prepAuth, Statement.RETURN_GENERATED_KEYS);
                insertAuth.setString(1, recipe.getAuthorFirst());
                insertAuth.setString(2, recipe.getAuthorLast());
                insertAuth.executeUpdate();

                //return the id of the last inserted author
                ResultSet authInsertedId = insertAuth.getGeneratedKeys();
                authInsertedId.next();
                authId = authInsertedId.getInt(1);
            } else {
                authId = id;
            }
            //now insert the recipe
            String prepRecipe = "insert into recipes (author_fk, title, difficulty, prep_time, inactive_time, cook_time, total_time, yield) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement insertRecipe = c.prepareStatement(prepRecipe, Statement.RETURN_GENERATED_KEYS);
            insertRecipe.setInt(1, authId);
            insertRecipe.setString(2, recipe.getTitle());
            insertRecipe.setString(3, recipe.getDifficulty());
            insertRecipe.setInt(4, recipe.getPrepTime());
            insertRecipe.setInt(5, recipe.getInactiveTime());
            insertRecipe.setInt(6, recipe.getCookTime());
            insertRecipe.setInt(7, recipe.getTotalTime());
            insertRecipe.setInt(8, recipe.getYield());
            insertRecipe.executeUpdate();
            //return the id of the last inserted recipe
            ResultSet recipeInsertedId = insertRecipe.getGeneratedKeys();
            recipeInsertedId.next();
            int recipeId = recipeInsertedId.getInt(1);
            String prepSteps = "insert into steps (recipe, step_order, step_text) values(?, ?, ?);";
            ArrayList<String> recipeSteps = recipe.getSteps();
            int sizeRec = recipeSteps.size();
            for (int i = 0; i < sizeRec; i++) {
                PreparedStatement insertSteps = c.prepareStatement(prepSteps, Statement.RETURN_GENERATED_KEYS);
                insertSteps.setInt(1, recipeId);
                insertSteps.setInt(2, i);
                insertSteps.setString(3, recipeSteps.get(i));
                insertSteps.executeUpdate();
            }
            String prepIngredients = "insert into ingredients (recipe, ingredient_text) values(?, ?);";
            ArrayList<String> recipeIngredients = recipe.getIngredients();
            int sizeIng = recipeIngredients.size();
            for (int i = 0; i < sizeIng; i++) {
                PreparedStatement insertIngredients = c.prepareStatement(prepIngredients, Statement.RETURN_GENERATED_KEYS);
                insertIngredients.setInt(1, recipeId);
                insertIngredients.setString(2, recipeIngredients.get(i));
                insertIngredients.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
    public void insertRecipe(Recipe recipe) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/recipe","kvansylyvong","password");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            //start by searching for the author, if does not exist then add and return id, otherwise return id
            String query = "Select * from authors where first_name = 'thomas' and last_name = 'keller';";
            ResultSet rs = stmt.executeQuery(query);

            if (!rs.next()) {
                this.insert(recipe, c, 0);

            } else {
                System.out.println("Found: " + rs.getString("first_name") + " " + rs.getString("last_name") + " " + rs.getInt("id"));
                System.out.println("Attaching recipe to existing author");
                this.insert(recipe, c, rs.getInt("id"));
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
   /* public Recipe searchRecipes(String recipe_name) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/recipe", "kvansylyvong", "password");
            Statement stmt = c.createStatement();
            String prepQuery = "select *, steps.step_text from recipes where title like ?";
            PreparedStatement findRecipe = c.prepareStatement(prepQuery);
            findRecipe.setString(2,"%" + recipe_name + "&");
            ResultSet rs = findRecipe.executeQuery();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return Recipe;
    } */
}