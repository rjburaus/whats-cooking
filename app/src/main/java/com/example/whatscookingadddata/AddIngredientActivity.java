package com.example.whatscookingadddata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddIngredientActivity extends AppCompatActivity {

    EditText mIngredientNameView;
    EditText mIngredientURLView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
        mIngredientNameView = findViewById(R.id.ingredientNameEditText);
        mIngredientURLView = findViewById(R.id.ingredientURLEditText);
        mContext = this;
        Button backButton = findViewById(R.id.ingredientBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button submitDataButton = findViewById(R.id.ingredientSubmitButton);
        submitDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingredientName = mIngredientNameView.getText().toString();
                mIngredientNameView.setText("");
                if (ingredientName.equals("")){
                    Toast.makeText(mContext, "Recipe name is blank", Toast.LENGTH_LONG).show();
                    return;
                }
                String ingredientURL = mIngredientURLView.getText().toString();
                mIngredientURLView.setText("");
                if (ingredientURL.equals("")){
                    Toast.makeText(mContext, "Recipe name is blank", Toast.LENGTH_LONG).show();
                    return;
                }
                Ingredient ingredient = new Ingredient(ingredientName,ingredientURL, new ArrayList<String>());
                FirebaseManager manager = new FirebaseManager();
                manager.addIngredientToDb(ingredient);
                Toast.makeText(mContext, "Ingredient submitted, check debug tag in logcat for progress messages. The database may take some time to update.", Toast.LENGTH_LONG).show();

            }
        });
    }
}
