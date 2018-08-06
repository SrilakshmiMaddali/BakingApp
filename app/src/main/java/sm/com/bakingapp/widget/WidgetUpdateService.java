package sm.com.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import sm.com.bakingapp.R;
import sm.com.bakingapp.Recipe;

public class WidgetUpdateService extends IntentService {

    public static final String ACTION_UPDATE_LIST_VIEW = "update_app_widget_list";
    public static final String RECIPE_KEY = "recipe";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent !=null){
            final String action = intent.getAction();
            if (ACTION_UPDATE_LIST_VIEW.equals(action)){
                Recipe recipe = intent.getParcelableExtra(RECIPE_KEY);
                handleActionUpdateListView(recipe);
            }
        }
    }

    private void handleActionUpdateListView(Recipe recipe) {
        if (recipe != null) {
            AppWidgetDataModel.saveRecipe(this, recipe);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));

        BakingAppWidgetProvider.updateAppWidgets(this,appWidgetManager,appWidgetIds);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_list);
    }

    public static void startActionUpdateListView(Context context, Recipe recipe) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(WidgetUpdateService.ACTION_UPDATE_LIST_VIEW);
        intent.putExtra(RECIPE_KEY, recipe);
        context.startService(intent);
    }
}
