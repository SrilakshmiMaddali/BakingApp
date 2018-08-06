package sm.com.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import sm.com.bakingapp.Ingredient;
import sm.com.bakingapp.R;
import sm.com.bakingapp.Recipe;
import sm.com.bakingapp.RecipeListItemDetailsActivity;

public class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    List<Ingredient> ingredients = new ArrayList<>();
    Recipe recipe = null;

    public AppWidgetListView(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        recipe = AppWidgetDataModel.getRecipe(mContext);
        ingredients = recipe.getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item_view);

        remoteViews.setTextViewText(R.id.ingredient_name_widget_tv, ingredients.get(position).getIngredient());
        remoteViews.setTextViewText(R.id.ingredient_amount_widget_tv,mContext.getResources().getString(R.string.ingredient_amount, ingredients.get(position).getQuantity(), ingredients.get(position).getMeasure()));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RecipeListItemDetailsActivity.RECIPE_EXTRA, recipe);
        remoteViews.setOnClickFillInIntent(R.id.parent_view, fillInIntent);
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
