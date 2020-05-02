package com.example.whatscookingadddata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class MainActivity extends AppCompatActivity {
    static Semaphore writeToDb = new Semaphore(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View recipeButton = findViewById(R.id.RecipeButton);
        View ingredientButton = findViewById(R.id.IngredientButton);
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRecipeActivity = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivity(startRecipeActivity);
            }
        });
        ingredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIngredientActivity = new Intent(MainActivity.this, AddIngredientActivity.class);
                startActivity(startIngredientActivity);
            }
        });
/*
        FirebaseManager manager = new FirebaseManager();
        Ingredient i = new Ingredient("Test Ingredient", "Test URL", new ArrayList<String>());
        manager.addIngredientToDb(i);
        List<String> testArray = new ArrayList<>();
        testArray.add("Test Ingredient");
        Recipe r = new Recipe("Test Recipe", "Test instruction", "Test URL", 0, testArray);
        Log.d(FirebaseManager.TAG, "Stage 2");
        manager.addRecipeToDb(r);
        Log.d(FirebaseManager.TAG, "Finished Program");
*/
    }
}
