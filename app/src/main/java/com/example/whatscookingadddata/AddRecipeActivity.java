package com.example.whatscookingadddata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    int mealType = 0;
    ArrayList<String> ingredientList = new ArrayList<String>();
    Context mContext = this;
    ListView mListView;
    EditText mRecipeNameView;
    EditText mRecipeURLView;
    EditText mRecipeInstructionsView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        mRecipeNameView = findViewById(R.id.recipeNameEditText);
        mRecipeURLView = findViewById(R.id.recipeURLEditText);
        mRecipeInstructionsView = findViewById(R.id.recipeInstructionEditText);
        Spinner spinner = findViewById(R.id.recipeMealTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.MealTypeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mListView = findViewById(R.id.list_view);
        MyListAdapter listAdapter = new MyListAdapter(this, ingredientList);
        mListView.setAdapter(listAdapter);
        Button backButton = findViewById(R.id.recipeBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button submitData = findViewById(R.id.recipeSubmitButton);
        submitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int submittedMealType = mealType;
                String recipeName = mRecipeNameView.getText().toString();
                mRecipeNameView.setText("");
                if (recipeName.equals("")){
                    Toast.makeText(mContext, "Recipe name is blank", Toast.LENGTH_LONG).show();
                    return;
                }
                String recipeInstructions = mRecipeInstructionsView.getText().toString();
                mRecipeInstructionsView.setText("");
                if (recipeInstructions.equals("")){
                    Toast.makeText(mContext, "Recipe instructions are blank", Toast.LENGTH_LONG).show();
                    return;
                }

                String recipeURL = mRecipeURLView.getText().toString();
                mRecipeURLView.setText("");
                if (recipeURL.equals("")){
                    Toast.makeText(mContext, "Recipe URL is blank", Toast.LENGTH_LONG).show();
                    return;
                }
                if (ingredientList.size() == 0){
                    Toast.makeText(mContext, "No ingredients in recipe", Toast.LENGTH_LONG).show();
                    return;
                }
                Recipe recipeToAdd = new Recipe(recipeName, recipeInstructions, recipeURL, submittedMealType, ingredientList);
                StringBuffer sb = new StringBuffer();
                /*Map<String, String> map = recipeToAdd.getRecipeIngredients();
                for (int i = 0; i < map.size(); i++){
                    sb.append(map.get(i+""));
                    sb.append(" ");
                }
                Log.d(FirebaseManager.TAG, "Ingredients in recipe list are: " + sb.toString());*/
                FirebaseManager manager = new FirebaseManager();
                manager.addRecipeToDb(recipeToAdd);
                ingredientList.clear();
                Toast.makeText(mContext, "Recipe submitted, check debug tag in logcat for progress messages. The database may take some time to update.", Toast.LENGTH_LONG).show();
                MyListAdapter listAdapter = new MyListAdapter(mContext, ingredientList);
                mListView.setAdapter(listAdapter);
            }
        });
        Button addIngredient = findViewById(R.id.addIngredientButton);
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ingredientEditText = findViewById(R.id.recipeIngredientEditText);
                if(ingredientEditText.getText().toString().equals("")){
                    Toast.makeText(mContext, "No ingredient entered", Toast.LENGTH_LONG).show();
                    return;
                }
                ingredientList.add(ingredientEditText.getText().toString());
                StringBuffer sb = new StringBuffer();
                for (String s : ingredientList){
                    sb.append(s);
                    sb.append(" ");
                }
                Log.d(FirebaseManager.TAG, "Ingredients in list are: " + sb.toString());
                ingredientEditText.setText("");
                MyListAdapter listAdapter = new MyListAdapter(mContext, ingredientList);
                mListView.setAdapter(listAdapter);
                Toast.makeText(mContext, "Ingredient added to list", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mealType = (int)l;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mealType = 0;
    }
}
