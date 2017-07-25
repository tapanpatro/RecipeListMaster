package com.tapan.recipemaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tapan.recipemaster.R;
import com.tapan.recipemaster.adapter.RecipeListAdapter;
import com.tapan.recipemaster.model.Ingredient;
import com.tapan.recipemaster.model.Recipe;
import com.tapan.recipemaster.model.Step;
import com.tapan.recipemaster.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeItemOnClickListenerInterface {


    @BindView(R.id.rv_recyclerView)
    RecyclerView mRecyclerView;

    AppConfig appConfig;

    ArrayList<Recipe> recipeArrayList;
    RecipeListAdapter recipeListAdapter;

    public final CountingIdlingResource countingIdlingResource = new CountingIdlingResource("CountingIdlingResource");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE},32);

        appConfig = new AppConfig(this);
        ButterKnife.bind(this);
        if (appConfig.isNetworkAvailable()) {
            RequestQueue requestQueue = Volley.newRequestQueue(RecipeListActivity.this);
            JsonArrayRequest jsonArrayRequest = getJsonArray(appConfig.BASE_URL);
            countingIdlingResource.increment();
            requestQueue.add(jsonArrayRequest);

            recipeArrayList = new ArrayList<>();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.no_of_columns));
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            Toast.makeText(RecipeListActivity.this, "Please check your Internet Connection! ", Toast.LENGTH_LONG).show();
        }

    }
    //sir can i know why this method didn't worked //every time it worked but this time it didn't
    private ArrayList<Recipe> getRecipeListData() {
        final ArrayList<Recipe> returnedRecipeList = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(appConfig.BASE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                int recipeId, servings;
                String name;
                ArrayList<Ingredient> ingredients;
                ArrayList<Step> steps;


                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody));

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        recipeId = jsonObject.getInt("id");
                        name = jsonObject.getString("name");
                        ingredients = new ArrayList<>();
                        steps = new ArrayList<>();
                        JSONArray ingredientsJsonArray = jsonObject.getJSONArray("ingredients");
                        JSONArray stepsJsonArray = jsonObject.getJSONArray("steps");
                        servings = jsonObject.getInt("servings");

                        for (int j = 0; j < ingredientsJsonArray.length(); j++) {
                            JSONObject ingredientsObject = ingredientsJsonArray.getJSONObject(j);
                            double quantity = ingredientsObject.getDouble("quantity");
                            String measure = ingredientsObject.getString("measure");
                            String ingredient = ingredientsObject.getString("ingredient");
                            ingredients.add(new Ingredient(quantity, measure, ingredient));
                        }

                        for (int j = 0; j < stepsJsonArray.length(); j++) {
                            JSONObject stepsJsonObject = stepsJsonArray.getJSONObject(j);
                            int stepId = stepsJsonObject.getInt("id");
                            String shortDescription = stepsJsonObject.getString("shortDescription");
                            String description = stepsJsonObject.getString("description");
                            String videoUrl = stepsJsonObject.getString("videoURL");
                            String thumbnailUrl = stepsJsonObject.getString("thumbnailURL");
                            steps.add(new Step(stepId, shortDescription, description, videoUrl, thumbnailUrl));
                        }

                        //returnedRecipeList.add(new Recipe(recipeId, name, ingredients, steps, servings));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


        return returnedRecipeList;
    }

    private JsonArrayRequest getJsonArray(String url) {
        return new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                init(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecipeListActivity.this, "No Internet Connection! ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init(JSONArray response) {
        recipeArrayList = parseRecipeJson(response);
        recipeListAdapter = new RecipeListAdapter(RecipeListActivity.this, recipeArrayList, RecipeListActivity.this);
        mRecyclerView.setAdapter(recipeListAdapter);
        mRecyclerView.invalidate();
        countingIdlingResource.decrement();
    }

    private ArrayList<Recipe> parseRecipeJson(JSONArray response) {
        ArrayList<Recipe> returnedRecipeList = new ArrayList<>();
        if (response != null) {
            try {
                int recipeId;
                String name;
                String imageUrl;
                ArrayList<Ingredient> ingredients;
                ArrayList<Step> steps;
                int servings;

                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    recipeId = obj.getInt("id");
                    name = obj.getString("name");
                    imageUrl = obj.getString("image");
                    ingredients = new ArrayList<>();
                    steps = new ArrayList<>();
                    JSONArray ingredientsJsonArray = obj.getJSONArray("ingredients");
                    JSONArray stepsJsonArray = obj.getJSONArray("steps");
                    servings = obj.getInt("servings");

                    for (int j = 0; j < ingredientsJsonArray.length(); j++) {
                        JSONObject ingredientsObject = ingredientsJsonArray.getJSONObject(j);
                        double quantity = ingredientsObject.getDouble("quantity");
                        String measure = ingredientsObject.getString("measure");
                        String ingredient = ingredientsObject.getString("ingredient");
                        ingredients.add(new Ingredient(quantity, measure, ingredient));
                    }

                    for (int j = 0; j < stepsJsonArray.length(); j++) {
                        JSONObject stepsJsonObject = stepsJsonArray.getJSONObject(j);
                        int stepId = stepsJsonObject.getInt("id");
                        String shortDescription = stepsJsonObject.getString("shortDescription");
                        String description = stepsJsonObject.getString("description");
                        String videoUrl = stepsJsonObject.getString("videoURL");
                        String thumbnailUrl = stepsJsonObject.getString("thumbnailURL");
                        steps.add(new Step(stepId, shortDescription, description, videoUrl, thumbnailUrl));
                    }

                    returnedRecipeList.add(new Recipe(recipeId, name,imageUrl, ingredients, steps, servings));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(RecipeListActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }


        return returnedRecipeList;
    }


    @Override
    public void onRecipeItemClicked(Recipe recipe) {

        //creating an intent for recipeDetailActivity with recipe obj as extra
        Intent intent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
        intent.putExtra(getString(R.string.intent_recipe), recipe);
        startActivity(intent);
    }


}
