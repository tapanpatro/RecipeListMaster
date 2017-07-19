package com.tapan.recipemaster;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tapan.recipemaster.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hp on 7/12/2017.
 */

public class WidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewFactory(this.getApplicationContext(),intent);
    }

    private class RecipeRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory{

        private final String TAG = RecipeRemoteViewFactory.class.getSimpleName();
        private Context context;
        private ArrayList<Recipe> recipeArrayList;
        private ArrayList<Ingredient> ingredientsList = new ArrayList<Ingredient>();
        private AppWidgetManager appWidgetManager;
        private int appWidgetId;

        AppConfig appConfig = new AppConfig(context);

        public RecipeRemoteViewFactory(Context context , Intent intent){
            recipeArrayList = new ArrayList<>();
            this.context = context;
            appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            //Make the http request here
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            if (isNetworkAvailableWidget()){
                JsonArrayRequest jsonArrayRequest = getJsonArray(appConfig.BASE_URL);
                requestQueue.add(jsonArrayRequest);
            }
            Log.v(TAG,"On create");
        }

        private JsonArrayRequest getJsonArray(String url) {
            return new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    recipeArrayList = parseRecipeJson(response);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_stackView);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG,"Volley error response");
                }
            });
        }

        private ArrayList<Recipe> parseRecipeJson(JSONArray response) {
            ArrayList<Recipe> returnedRecipeList= new ArrayList<>();

            if (response !=null){
                try{
                    int recipeId;
                    String name;
                    ArrayList<Ingredient> ingredients;
                    ArrayList<Step> steps;
                    int servings;

                    for (int i=0;i<response.length();i++){
                        JSONObject obj = response.getJSONObject(i);
                        recipeId = obj.getInt("id");
                        name = obj.getString("name");
                        ingredients = new ArrayList<>();
                        steps = new ArrayList<>();
                        JSONArray ingredientsJsonArray = obj.getJSONArray("ingredients");
                        JSONArray stepsJsonArray = obj.getJSONArray("steps");
                        servings = obj.getInt("servings");

                        for (int j=0;j<ingredientsJsonArray.length();j++){
                            JSONObject ingredientsObject = ingredientsJsonArray.getJSONObject(j);
                            double quantity = ingredientsObject.getDouble("quantity");
                            String measure = ingredientsObject.getString("measure");
                            String ingredient = ingredientsObject.getString("ingredient");
                            ingredients.add(new Ingredient(quantity,measure,ingredient));
                        }

                        for (int j=0;j<stepsJsonArray.length();j++){
                            JSONObject stepsJsonObject = stepsJsonArray.getJSONObject(j);
                            int stepId= stepsJsonObject.getInt("id");
                            String shortDescription = stepsJsonObject.getString("shortDescription");
                            String description = stepsJsonObject.getString("description");
                            String videoUrl = stepsJsonObject.getString("videoURL");
                            String thumbnailUrl = stepsJsonObject.getString("thumbnailURL");
                            steps.add(new Step(stepId,shortDescription,description,videoUrl,thumbnailUrl));
                        }
                        returnedRecipeList.add(new Recipe(recipeId,name,ingredients,steps,servings));
                    }
                }catch (JSONException e){
                    e.printStackTrace();

                }
            }
            return returnedRecipeList;
        }

        @Override
        public void onDataSetChanged() {
            Log.v(TAG,"Data changed");
        }

        @Override
        public void onDestroy() {
            recipeArrayList.clear();
            ingredientsList.clear();
        }

        @Override
        public int getCount() {
            return recipeArrayList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.single_item_widget);
            if (recipeArrayList !=null){
                ingredientsList = recipeArrayList.get(position).ingredientsArrayList;
                /*StringBuilder ingredientDescription = new StringBuilder();
                for (Ingredient ingredient: ingredientsList){
                    ingredientDescription.append(" * ");
                    ingredientDescription.append(ingredient.quantity);
                    ingredientDescription.append(" ");
                    ingredientDescription.append(ingredient.measure);
                    ingredientDescription.append(" - ");
                    ingredientDescription.append(ingredient.ingredients);
                    ingredientDescription.append("\n");
                }*/
                remoteViews.setTextViewText(R.id.widget_stackview_recipe_title,recipeArrayList.get(position).name);
                remoteViews.setTextViewText(R.id.widget_stackView_recipe_ingredients,String.valueOf(ingredientsList.get(position).quantity));
                Bundle bundle = new Bundle();
                bundle.putInt("extra_position", position);
                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(bundle);
            }else {

            }
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    private boolean isNetworkAvailableWidget() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();


        return activeNetworkInfo.isConnected();
    }

}
