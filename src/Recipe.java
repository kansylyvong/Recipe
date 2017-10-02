import java.lang.reflect.Array;
import java.util.*;

public class Recipe {
    protected String authorFirstName;
    protected String authorLastName;
    protected String title;
    protected ArrayList<String> ingredients;
    protected ArrayList<String> steps;
    protected String difficulty;
    protected double rating;
    protected int views;
    protected int prepTime;
    protected int inactiveTime;
    protected int cookTime;
    protected int totalTime;
    protected int yield;
    private int recipeId;
    private int authorId;
    protected ArrayList<String> categories;
    protected ArrayList<String> comments;
    protected HashMap<Integer, String> returnedSteps = new HashMap<>();
    protected HashMap<Integer, String> returnedIngredients = new HashMap<>();

    public Recipe(String author_first, String author_last, String title, ArrayList<String> ingredients, ArrayList<String> steps, String difficulty, int prepTime, int inactiveTime, int cookTime, int yield /*, ArrayList<String> categories */) {
        super();
        this.authorFirstName = author_first;
        this.authorLastName = author_last;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.difficulty = difficulty;
        this.prepTime = prepTime;
        this.inactiveTime = inactiveTime;
        this.cookTime = cookTime;
        this.totalTime = this.prepTime + this.inactiveTime + this.cookTime;
        this.yield = yield;
        this.categories = categories;
    }
    public String getAuthorFirst() { return this.authorFirstName; }
    public String getAuthorLast() { return this.authorLastName; }
    public String getTitle() {
        return this.title;
    }

    public ArrayList<String> getIngredients() {
        return this.ingredients;
    }
    public ArrayList<String> getSteps() {
        return this.steps;
    }
    public String getDifficulty() {
        return this.difficulty;
    }
    public int getPrepTime() {
        return this.prepTime;
    }
    public int getInactiveTime() {
        return this.inactiveTime;
    }
    public int getCookTime() {
        return this.cookTime;
    }
    public int getTotalTime() {
        return this.totalTime;
    }
    public int getYield() {
        return this.yield;
    }
    public ArrayList<String> getCategories() {
        return this.categories;
    }
    public ArrayList<String> getComments() {
        return this.comments;
    }
    public int getRecipeId() { return this.recipeId; }
    public int getAuthorId() { return this.authorId; }
    public HashMap<Integer, String> getReturnedSteps() {
        return returnedSteps;
    }
    public HashMap<Integer, String> getReturnedIngredients() {
        return returnedIngredients;
    }

    //setters
    public void setIngredients (ArrayList<String> ingredients) { this.ingredients = ingredients; }
    public void setSteps(ArrayList<String> steps) { this.steps = steps; }
    public void setDifficulty (String difficulty) { this.difficulty = difficulty; }
    public void setPrepTime (int prepTime) { this.prepTime = prepTime; }
    public void setInactiveTime(int inactiveTime) { this.inactiveTime = inactiveTime; }
    public void setCookTime(int cookTime) { this.cookTime = cookTime; this.totalTime = this.cookTime + this.inactiveTime + this.prepTime;}
    public void setYield (int Yield) { this.yield = yield; }
    public void addIngredients (ArrayList<String> ingredients) {
        for (String item: ingredients) {
            this.ingredients.add(item);
        }
    }
    public void addSteps(ArrayList<String> steps) {
        for (String step: steps) {
            this.steps.add(step);
        }
    }
    public void addCategories (ArrayList<String> categories) {
        for (String item: categories) {
            this.categories.add(item);
        }
    }
    public void addComments(ArrayList<String> comments) {
        for (String item: comments) {
            this.comments.add(item);
        }
    }
    public void setRecipeId(int id) {
        this.recipeId = id;
    }
    public void setAuthorId(int id) {
        this.authorId = id;
    }
    public void addReturnedStep(Integer key, String value) {
        this.returnedSteps.put(key, value);
    }
    public void addReturnedIngredients(Integer key, String value) {
        this.returnedIngredients.put(key, value);
    }
}
