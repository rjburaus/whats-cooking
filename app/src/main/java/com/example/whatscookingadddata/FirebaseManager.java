package com.example.whatscookingadddata;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;


import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseManager {
    private FirebaseAuth auth;
    private FirebaseFirestore dbInstance;
    private final String EMAIL = "sd84470n@pace.edu";
    private final String PASSWORD = "password";
    private final String INGREDIENTCOLLECTION = "Ingredients";
    private final String INGREDIENTURLSTRING = "ingredientURL";
    private final String DEPENDANTRECIPES = "dependentRecipes";
    private final String RECIPECOLLECTION = "Recipes";
    private final String RECIPEINSTRUCTIONS = "Recipe Instructions";
    final static String TAG = "debug";
    FirebaseManager(){
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    try {
                        throw new Exception("Failed to authenticate to Firebase");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        dbInstance = FirebaseFirestore.getInstance();
    }

    void addIngredientToDb(Ingredient i) {
        final Ingredient ingredient = i;
        assert (ingredient != null);
        final String fIngredientName = cleanString(ingredient.getIngredientName());
        Log.d(TAG, "Preparing to add ingredient " + fIngredientName);
        final DocumentReference docToCheck = dbInstance.collection(INGREDIENTCOLLECTION).document(fIngredientName);
        dbInstance.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(docToCheck);
                if (snapshot.exists())
                {
                    Log.d(TAG, fIngredientName + " already exists, updating existing ingredient");
                    transaction.update(docToCheck, INGREDIENTURLSTRING, ingredient.getIngredientURL());
                }
                else
                {
                    Log.d(TAG, fIngredientName + " does not exist, adding ingredient");
                    transaction.set(docToCheck, ingredient);
                }
                Log.d(TAG, "Sent request to database, awaiting response...");
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, fIngredientName + " updated successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Ingredient update failed. " + e);
            }
        });
    }

    public void addRecipeToDb(Recipe r){
        final Recipe recipe = r;
        final String recipeName = cleanString(recipe.getRecipeName());
        final DocumentReference docToCheck = dbInstance.collection(RECIPECOLLECTION).document(recipeName);
        final WriteBatch batch = dbInstance.batch();
        Log.d(TAG, "About to start transaction...");
        dbInstance.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(docToCheck);
                if (snapshot.exists())
                {
                    Recipe oldRecipe = snapshot.toObject(Recipe.class);
                    assert (oldRecipe.getRecipeIngredients() != null);
                    if(oldRecipe.getRecipeIngredients().equals(recipe.getRecipeIngredients()))
                    {
                        Log.d(TAG, "Recipe already exists, updating existing recipe");
                        transaction.set(docToCheck, recipe);
                    }
                    else
                    {
                        Log.d(TAG, "Detected recipe conflict, please delete old recipe first if this was intentional");
                        throw new FirebaseFirestoreException("One of the new recipe ingredients conflicts with the old recipe ingredients. Please delete the old recipe first", FirebaseFirestoreException.Code.FAILED_PRECONDITION);
                    }
                }
                else
                {
                    Map<String, String> ingredients = recipe.getRecipeIngredients();
                    ArrayList<String> ingredientArray = new ArrayList<String>();
                    for (int a = 0; a <ingredients.size(); a++){
                        String temp = ingredients.get(a+"");
                        ingredientArray.add(temp);
                    }


                    for (int i = 0; i < ingredients.size(); i++)
                    {
                        String currentIngredient = cleanString(ingredientArray.get(i));
                        Log.d(TAG, "Preparing to get " + currentIngredient);
                        DocumentReference ingredientToCheck = dbInstance.collection(INGREDIENTCOLLECTION).document(currentIngredient);
                        DocumentSnapshot ingredientSnapshot = transaction.get(ingredientToCheck);
                        if (ingredientSnapshot.exists())
                        {
                            Log.d(TAG, currentIngredient + " exists, updating...");
                            batch.update(ingredientToCheck, DEPENDANTRECIPES, FieldValue.arrayUnion(recipeName));
                        }
                        else
                        {
                            Log.d(TAG, currentIngredient + " recipe ingredient not in database, aborting database update...");
                            throw new FirebaseFirestoreException("One of the new recipe ingredients is not added to the database, please add that ingredient first", FirebaseFirestoreException.Code.FAILED_PRECONDITION);
                        }
                    }
                    transaction.set(docToCheck, recipe);
                }

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, recipeName + " updated successfully");
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully updated all ingredients");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to update ingredients " + e);
                        Log.d(TAG, "Warning, ingredients and recipes may be out of sync in database");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Recipe update failed. " + e);
                e.printStackTrace();
            }
        });

    }
    private static String cleanString(String s){
        String string = s;
        string = string.replaceAll("\\s+","");
        string = string.toUpperCase();
        return string;

    }

}
