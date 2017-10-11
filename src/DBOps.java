import java.sql.*;
import java.util.*;


public class DBOps{
    //method to insert a recipe
    private void insert(Recipe recipe, Connection c, Integer id) {
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
            insertRecipe.setString(8, recipe.getYield());
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
            //start by searching for the author, if does not exist then add and return id, otherwise return id
            String query = "Select * from authors where first_name = ? and last_name = ?;";
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, recipe.getAuthorFirst());
            ps.setString(2, recipe.getAuthorLast());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                this.insert(recipe, c, 0);

            } else {
                System.out.println("Found: " + rs.getString("first_name") + " " + rs.getString("last_name") + " " + rs.getInt("id"));
                System.out.println("Attaching recipe to existing author");
                this.insert(recipe, c, rs.getInt("id"));
            }
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    //helper method for constructing returned recipes
    private ArrayList<Recipe> constructReturnedRecipes(ResultSet rs) {
        String first_name, last_name, title, difficulty, yield;
        int inactiveTime, prepTime, cookTime, totalTime;
        ArrayList<String> steps;
        ArrayList<String> ingredients;
        HashMap<Integer, String> stepList = new HashMap<>();
        HashMap<Integer, String> ingredientList = new HashMap<>();
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        String[] tempSteps;
        String[] tempIngredients;
        try {
            while (rs.next()) {
                first_name = rs.getString("first_name");
                last_name = rs.getString("last_name");
                title = rs.getString("title");
                difficulty = rs.getString("difficulty");
                inactiveTime = rs.getInt("inactive_time");
                prepTime = rs.getInt("prep_time");
                cookTime = rs.getInt("cook_time");
                totalTime = rs.getInt("total_time");
                yield = rs.getString("yield");
                steps = new ArrayList<>(Arrays.asList(rs.getString("steps").split("\\|")));
                ingredients = new ArrayList<>(Arrays.asList(rs.getString("ingredients").split("\\|")));

                Recipe recipe = new Recipe(first_name, last_name, title, ingredients, steps, difficulty, prepTime, inactiveTime, cookTime, yield);
                recipe.setRecipeId(rs.getInt("id"));
                recipe.setAuthorId(rs.getInt("author_fk"));
                for (String step : steps) {
                    tempSteps = step.split(">");
                    recipe.addReturnedStep(Integer.parseInt(tempSteps[0]), tempSteps[1].replace("<", " OrderNum:"));
                }
                for (String ingredient : ingredients) {
                    tempIngredients = ingredient.split(">");
                    recipe.addReturnedIngredients(Integer.parseInt(tempIngredients[0]), tempIngredients[1]);
                }


                recipes.add(recipe);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return recipes;
    }
    //Does a general search of recipes based on title, ingredients or steps
    public ArrayList<Recipe> searchRecipes(String recipe_name) {

        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/recipe", "kvansylyvong", "password");
            String prepQuery = "select r.*, s.recipe as step_fk, i.recipe as ing_fk, r.title, a.first_name, a.last_name, a.id as author_id, group_concat(distinct concat(s.step_id, \">\", s.step_text, \"<\", s.step_order) order by s.step_order asc SEPARATOR '|') as steps, group_concat(distinct concat(i.ingredient_id, \">\", i.ingredient_text) SEPARATOR '|') as ingredients, s.step_id, i.ingredient_id " +
                    "from recipes as r inner join authors as a " +
                    "on r.author_fk = a.id  " +
                    "left join ingredients as i " +
                    "on r.id = i.recipe  " +
                    "right join steps as s on s.recipe = r.id  " +
                    "where i.ingredient_text like ? or r.title like ? or s.step_text like ? " +
                    "group by r.id;";

            PreparedStatement findRecipe = c.prepareStatement(prepQuery);
            findRecipe.setString(1,"%" + recipe_name + "%");
            findRecipe.setString(2,"%" + recipe_name + "%");
            findRecipe.setString(3,"%" + recipe_name + "%");

            ResultSet rs = findRecipe.executeQuery();
            recipes = constructReturnedRecipes(rs);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return recipes;
    }
    //method to search recipe based on ingredients. Recipe must have at least the ingredients passed in
    public ArrayList<Recipe> ingredientMatch(String[] ingredients) {
        ArrayList<Recipe> recipe = new ArrayList<>();
        String arrIngredients = "";
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/recipe", "kvansylyvong", "password");
            String prepQuery = "select d.* from " +
                    "(select r.difficulty, r.id, r.author_fk, r.prep_time, r.inactive_time, r.cook_time, r.total_time, r.yield, s.recipe as step_fk, i.recipe as ing_fk, r.title, a.first_name, a.last_name, a.id as author_id, group_concat(distinct concat(s.step_id, \">\", s.step_text, \"<\", s.step_order) order by s.step_order asc SEPARATOR '|') as steps, group_concat(distinct concat(i.ingredient_id, \">\", i.ingredient_text) SEPARATOR '|') as ingredients " +
                    "from recipes as r inner join authors as a " +
                    "on r.author_fk = a.id  " +
                    "left join ingredients as i " +
                    "on r.id = i.recipe  " +
                    "right join steps as s on s.recipe = r.id  " +
                    "where ";


            for (int i=0;i<ingredients.length;i++) {
                arrIngredients += "i.ingredient_text like ? or ";
            }
            //remove last 'and'

            arrIngredients = arrIngredients.substring(0, arrIngredients.length() - 3);
            prepQuery += arrIngredients;
            prepQuery += "group by r.id) as d where ";


            for (int i=0;i<ingredients.length;i++) {
                prepQuery += "d.ingredients like ? and ";
            }
            //remove the last and
            prepQuery = prepQuery.substring(0, prepQuery.length() -5);
            prepQuery += ";";
            System.out.println(prepQuery);
            PreparedStatement findRecipe = c.prepareStatement(prepQuery);
            //Since the parameters are the same in the subquery and derived table, iterate through the ingredient list twice
            int it = 0;
            int i = 0;
            while (i < ingredients.length) {
                findRecipe.setString(it + 1, "%" + ingredients[i] + "%");
                it++;
                i++;
            }
            i = 0;
            while (i < ingredients.length) {
                findRecipe.setString(it+1, "%" + ingredients[i] + "%");
                it++;
                i++;
            }
            ResultSet rs = findRecipe.executeQuery();
            recipes = constructReturnedRecipes(rs);
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return recipes;
    }
    public void udpateRecipeIngredient(Recipe recipe) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/recipe", "kvansylyvong", "password");
            Statement stmt = c.createStatement();
            String prepUpdate = "update ingredients set ingredient_text = ? where  ";
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
