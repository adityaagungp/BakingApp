package com.aditya.bakingapp.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.object.Ingredient;
import com.aditya.bakingapp.object.Recipe;
import com.aditya.bakingapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIngredients = new ArrayList<>();

    public ListRemoteViewsFactory(Context context) {
        mContext = context;
        updateIngredients();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        updateIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients == null) {
            return 0;
        } else return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = mIngredients.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredient);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ingredient.getQuantity()).append(' ')
                .append(ingredient.getMeasure()).append(' ')
                .append(mContext.getString(R.string.of)).append(' ')
                .append(ingredient.getIngredient());
        views.setTextViewText(R.id.ingredientDetail, new String(stringBuilder));
        return views;
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

    private void updateIngredients() {
        SharedPreferences preferences = mContext.getApplicationContext().getSharedPreferences(Constants.PREFERENCES, 0);
        long recipeId = preferences.getLong(Constants.Param.RECIPE_ID, -1);
        if (recipeId != -1){
            Realm realm = Realm.getDefaultInstance();
            Recipe recipe = realm.where(Recipe.class).equalTo("id", recipeId).findFirst();
            if (recipe != null) {
                mIngredients = recipe.getIngredients();
            } else {
                mIngredients = new ArrayList<>();
            }
        } else {
            mIngredients = new ArrayList<>();
        }
    }
}
