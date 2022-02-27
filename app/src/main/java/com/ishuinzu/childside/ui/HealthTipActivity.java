package com.ishuinzu.childside.ui;

import static com.ishuinzu.childside.app.Utils.setWindowsFlags;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.adapter.FoodItemAdapter;
import com.ishuinzu.childside.databinding.ActivityHealthTipBinding;
import com.ishuinzu.childside.dialog.LoadingDialog;
import com.ishuinzu.childside.object.FoodItemObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HealthTipActivity extends AppCompatActivity {
    private static final String TAG = "HealthTipActivity";
    private ActivityHealthTipBinding binding;

    private int intCalories;
    private List<FoodItemObject> foodItemObjects;
    private FoodItemAdapter foodItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHealthTipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        // Transparent Status Bar
        setWindowsFlags(HealthTipActivity.this, new int[]{WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION}, true);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        if (getIntent().getExtras() != null) {
            String calories = "" + getIntent().getExtras().getString("CALORIES");
            intCalories = (int) Double.parseDouble(calories.substring(0, (calories.length() - 4)));
        }

        searchInDatabase(intCalories);
        setDefaults();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void searchInDatabase(int calories) {
        LoadingDialog.showLoadingDialog(HealthTipActivity.this);
        foodItemObjects = new ArrayList<>();
        foodItemAdapter = new FoodItemAdapter(getApplicationContext(), foodItemObjects, getLayoutInflater(), calories);
        binding.recyclerFoodItems.setAdapter(foodItemAdapter);
        binding.progressCaloriesBurnt.setProgress(calories, 1000);

        FirebaseDatabase.getInstance().getReference().child("health_api_data")
                .child("" + calories)
                .get()
                .addOnCompleteListener(searchAPIDataTask -> {
                    if (searchAPIDataTask.isSuccessful()) {
                        if (searchAPIDataTask.getResult() != null) {
                            if (searchAPIDataTask.getResult().getChildrenCount() != 0) {
                                for (DataSnapshot foodItem : searchAPIDataTask.getResult().getChildren()) {
                                    FoodItemObject foodItemObject = foodItem.getValue(FoodItemObject.class);

                                    foodItemObjects.add(foodItemObject);
                                }
                                foodItemAdapter.notifyDataSetChanged();
                                Log.d(TAG, "FETCHED FROM DATABASE");
                                LoadingDialog.closeDialog();
                            } else {
                                getHealthTip(calories);
                            }
                        } else {
                            getHealthTip(calories);
                        }
                    } else {
                        getHealthTip(calories);
                    }
                });
    }

    private void getHealthTip(int calories) {
        Log.d(TAG, "FETCHING FROM API");
        new RetrieveFoodsTask().execute("https://api.spoonacular.com/recipes/complexSearch?maxCalories=" + calories + "&apiKey=c17f65b2b1964a57ac5b030e8fc45f8f");
    }

    @SuppressLint("SetTextI18n")
    private void setDefaults() {
        // Get Default Wallpaper
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(HealthTipActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Drawable drawable = wallpaperManager.getDrawable();
        findViewById(R.id.mainLayout).setBackground(drawable);
    }

    class RetrieveFoodsTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String url = strings[0];
            StringBuilder inline = new StringBuilder();

            try {
                foodItemObjects.clear();
                URL getURL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) getURL.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != 200) {
                    throw new RuntimeException("HttpResponseCode " + responseCode);
                } else {
                    Scanner scanner = new Scanner(getURL.openStream());
                    while (scanner.hasNext()) {
                        inline.append(scanner.nextLine());
                    }
                    scanner.close();

                    // JSON Parsing
                    JsonParser jsonParser = new JsonParser();

                    // Json Element
                    JsonElement jsonElement = jsonParser.parse(inline.toString());

                    Log.d(TAG, "RESPONSE : " + jsonElement.toString());

                    // Convert To Json Array
                    JsonArray jsonElements = jsonElement.getAsJsonObject().getAsJsonArray("results");

                    for (JsonElement foodItem : jsonElements) {
                        // Title
                        String title = foodItem.getAsJsonObject().get("title").toString();
                        String image_link = foodItem.getAsJsonObject().get("image").toString();
                        Log.d(TAG, "TITLE : " + title);
                        Log.d(TAG, "IMAGE LINK : " + image_link);

                        // Calories
                        JsonElement nutritionElement = foodItem.getAsJsonObject().getAsJsonObject("nutrition");

                        // Convert To Json Array
                        JsonArray jsonNutrients = nutritionElement.getAsJsonObject().getAsJsonArray("nutrients");

                        double calories = 0;
                        for (JsonElement jsonNutrient : jsonNutrients) {
                            calories = calories + jsonNutrient.getAsJsonObject().get("amount").getAsDouble();
                        }

                        Log.d(TAG, "CALORIES : " + calories);

                        // Add To Food Item
                        FoodItemObject foodItemObject = new FoodItemObject();
                        foodItemObject.setCalories(calories);
                        foodItemObject.setImage_link(image_link.substring(1, image_link.length() - 1));
                        foodItemObject.setTitle(title.substring(1).substring(0, title.substring(1).length() - 1));

                        foodItemObjects.add(foodItemObject);
                    }
                }
            } catch (IOException e) {
                LoadingDialog.closeDialog();
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            foodItemAdapter.notifyDataSetChanged();
            uploadAPIData(foodItemObjects);
        }
    }

    private void uploadAPIData(List<FoodItemObject> foodItemObjects) {
        Log.d(TAG, "SIZE : " + foodItemObjects.size());
        for (int i=0; i<foodItemObjects.size(); i++) {
            FirebaseDatabase.getInstance().getReference().child("health_api_data")
                    .child("" + intCalories)
                    .child("food_" + i)
                    .setValue(foodItemObjects.get(i))
                    .addOnCompleteListener(updateAPIDataTask -> {
                        if (updateAPIDataTask.isSuccessful()) {
                            Log.d(TAG, "API DATA UPDATED");
                        }
                    });
        }
        LoadingDialog.closeDialog();
    }
}