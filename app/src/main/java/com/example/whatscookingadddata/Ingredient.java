package com.example.whatscookingadddata;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Ingredient {
    private String ingredientName;
    private String ingredientURL;
    private List<String> dependentRecipes;
    public Ingredient(){}
    public Ingredient(String IngredientName, String IngredientURL, ArrayList<String> DependentRecipes){
        ingredientName = IngredientName;
        ingredientURL = IngredientURL;
        dependentRecipes = DependentRecipes;
    }


    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setIngredientURL(String ingredientURL) {
        this.ingredientURL = ingredientURL;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getIngredientURL() {
        return ingredientURL;
    }


    public List<String> getDependentRecipes() {
        return dependentRecipes;
    }

    public void setDependentRecipes(List<String> dependentRecipes) {
        this.dependentRecipes = dependentRecipes;
    }
}
