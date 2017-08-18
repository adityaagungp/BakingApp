package com.aditya.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.object.Recipe;
import com.aditya.bakingapp.util.Constants;

import io.realm.Realm;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RecipeUpdateService extends IntentService {

    private static final String ACTION_UPDATE_WIDGET = "com.aditya.bakingapp.widget.action.update";

    public RecipeUpdateService() {
        super("RecipeUpdateService");
    }

    public static void startActionUpdate(Context context) {
        Intent intent = new Intent(context, RecipeUpdateService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdate();
            }
        }
    }

    private void handleActionUpdate() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Constants.PREFERENCES, 0);
        long recipeId = preferences.getLong(Constants.Param.RECIPE_ID, -1);
        String recipeName = "";
        Realm realm = Realm.getDefaultInstance();
        Recipe recipe = realm.where(Recipe.class).equalTo("id", recipeId).findFirst();
        if (recipe != null){
            recipeName = recipe.getName();
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list);
        BakingWidget.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, recipeId, recipeName);
    }
}
