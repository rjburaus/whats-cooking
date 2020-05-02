package com.example.whatscookingadddata;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recipe {
    private String recipeName;
    private String recipeInstruction;
    private String recipeURL;
    private HashMap<String, String> recipeIngredients = new HashMap<String, String>();
    private int mealType; // 0 = Breakfast, 1 = Lunch, 2 = Dinner, 3 = Snacks
    public Recipe(){}
    public Recipe(String RecipeName, String RecipeInstruction, String RecipeURL, int MealType, ArrayList<String> RecipeIngredients){
        recipeName = RecipeName;
        recipeInstruction = RecipeInstruction;
        recipeURL = RecipeURL;
        mealType = MealType;
        StringBuffer sb = new StringBuffer();
        for (String s : RecipeIngredients){
            sb.append(s);
            sb.append(" ");
        }
        Log.d(FirebaseManager.TAG, "Ingredients in Recipe class list are: " + sb.toString());
        for (int i = 0; i< RecipeIngredients.size(); i++){
            String index = String.valueOf(i);
            recipeIngredients.put(index, RecipeIngredients.get(i));
        }
        Log.d(FirebaseManager.TAG, "Map to String " + recipeIngredients.toString());
    }
    public Recipe(String RecipeName, String RecipeInstruction, String RecipeURL, int MealType, HashMap<String, String> RecipeIngredients){
        recipeName = RecipeName;
        recipeInstruction = RecipeInstruction;
        recipeURL = RecipeURL;
        mealType = MealType;
        recipeIngredients = RecipeIngredients;

    }
    public void setMealType(int mealType) {
        this.mealType = mealType;
    }



    public void setRecipeInstruction(String recipeInstruction) {
        this.recipeInstruction = recipeInstruction;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeURL(String recipeURL) {
        this.recipeURL = recipeURL;
    }

    public String getRecipeInstruction() {
        return recipeInstruction;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeURL() {
        return recipeURL;
    }

    public int getMealType() {
        return mealType;
    }

    public HashMap<String, String> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(HashMap<String, String> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
}
