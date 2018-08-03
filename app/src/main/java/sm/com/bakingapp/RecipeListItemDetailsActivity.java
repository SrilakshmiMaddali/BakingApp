package sm.com.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import sm.com.bakingapp.adapter.RecipeDetailsListItemAdapter;
import sm.com.bakingapp.util.IngredientFragment;
import sm.com.bakingapp.util.RecipeListItemDetailsFragment;
import sm.com.bakingapp.util.VideoPlayerFragment;

public class RecipeListItemDetailsActivity extends AppCompatActivity implements RecipeDetailsListItemAdapter.ItemListener {
    public static String RECIPE_EXTRA = "recipes_extra";
    private boolean mTwoPane;
    android.support.v4.app.FragmentManager mFragmentManager;
    Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details_list_activity);
        mFragmentManager = getSupportFragmentManager();

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            }

            recipe = intent.getParcelableExtra(RECIPE_EXTRA);

            Bundle bundle = new Bundle();
            bundle.putStringArrayList(RecipeListItemDetailsFragment.LIST_NAMES_EXTRA, recipe.getShortDescriptionsFromSteps());
            RecipeListItemDetailsFragment listFragment = new RecipeListItemDetailsFragment();
            listFragment.setArguments(bundle);

            mFragmentManager.beginTransaction()
                    .add(R.id.detail_recipe_list_fragment, listFragment)
                    .commit();
        } else {
            recipe = savedInstanceState.getParcelable(RECIPE_EXTRA);
        }

        setTitle(recipe.getName());
        mTwoPane = findViewById(R.id.player_fragment) != null;
    }

    private void closeOnError() {
        finish();
    }

    @Override
    public void onItemSelected(int position) {
        if (!mTwoPane) {
            if (position == 0) {
                Intent ingredientsIntent = new Intent(RecipeListItemDetailsActivity.this, IngredientActivity.class);
                ingredientsIntent.putParcelableArrayListExtra(IngredientActivity.INGREDIENTS_EXTRA, (ArrayList<? extends Parcelable>) recipe.getIngredients());
                startActivity(ingredientsIntent);
            } else {
                Intent stepIntent = new Intent(RecipeListItemDetailsActivity.this, StepActivity.class);
                stepIntent.putParcelableArrayListExtra(StepActivity.STEPS_EXTRA, (ArrayList<? extends Parcelable>) recipe.getSteps());
                stepIntent.putExtra(StepActivity.POS_EXTRA, position - 1);
                stepIntent.putExtra(StepActivity.RECIPE_NAME_EXTRA, recipe.getName());
                startActivity(stepIntent);
            }
        }
        else {
            if (position == 0) {
                IngredientFragment ingredientsFragment = new IngredientFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(IngredientFragment.INGREDIENTS_EXTRA, (ArrayList<? extends Parcelable>) recipe.getIngredients());
                ingredientsFragment.setArguments(bundle);

                mFragmentManager.beginTransaction()
                        .replace(R.id.player_fragment,ingredientsFragment)
                        .commit();
            }else{
                List<Step> steps = recipe.getSteps();
                int stepPos = position - 1;
                String url0 = steps.get(stepPos).getVideoURL();
                String url1 = steps.get(stepPos).getThumbnailURL();
                String videoUrl = (url1.equals(""))?url0:url1;
                String stepDescription = steps.get(stepPos).getDescription();

                Bundle bundle = new Bundle();
                bundle.putString(VideoPlayerFragment.DESCRIPTION_EXTRA,stepDescription);
                bundle.putString(VideoPlayerFragment.VIDEO_URL_EXTRA,videoUrl);
                VideoPlayerFragment playerFragment = new VideoPlayerFragment();
                playerFragment.setArguments(bundle);

                mFragmentManager.beginTransaction()
                        .replace(R.id.player_fragment, playerFragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_EXTRA, recipe);
    }
}
