package com.aditya.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.home.MainActivity;
import com.aditya.bakingapp.recipe.RecipeActivity;
import com.aditya.bakingapp.util.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, long recipeId, String recipeName) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews view;
        if (width < 200) {
            view = getSingleRecipeRemoteView(context, recipeId, recipeName);
        } else {
            view = getListIngredientsRemoteView(context, recipeId, recipeName);
        }
        appWidgetManager.updateAppWidget(appWidgetId, view);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, long recipeId, String recipeName) {
        for (int id : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, id, recipeId, recipeName);
        }
    }

    private static RemoteViews getSingleRecipeRemoteView(Context context, long recipeId, String recipeName) {
        Intent intent;
        if (recipeId == -1) {
            intent = new Intent(context, MainActivity.class);
        } else {
            intent = new Intent(context, RecipeActivity.class);
            intent.putExtra(Constants.Param.RECIPE_ID, recipeId);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_small);
        if (recipeName.equals("")){
            recipeName = context.getString(R.string.goto_app);
        }
        views.setTextViewText(R.id.recipeName, recipeName);
        views.setOnClickPendingIntent(R.id.recipeIcon, pendingIntent);
        return views;
    }

    private static RemoteViews getListIngredientsRemoteView(Context context, long recipeId, String recipeName) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list);
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.list, intent);

        views.setTextViewText(R.id.title, String.format(context.getString(R.string.ingredients_of), recipeName));
        views.setTextViewText(R.id.emptyIngredient, String.format(context.getString(R.string.no_ingredient), recipeName));
        views.setEmptyView(R.id.list, R.id.emptyView);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeUpdateService.startActionUpdate(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                                          Bundle newOptions) {
        RecipeUpdateService.startActionUpdate(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

