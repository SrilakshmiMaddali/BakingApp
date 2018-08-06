package sm.com.bakingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.com.bakingapp.adapter.RecipesAdapter;
import sm.com.bakingapp.idlingResource.SimpleIdlingResource;
import sm.com.bakingapp.util.RetrofitClient;
import sm.com.bakingapp.widget.WidgetUpdateService;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";
    public static String RECIPES_LIST_EXTRA = "recipes_list";
    @Nullable
    private SimpleIdlingResource mIdlingResource;
    List<Recipe> mRecipes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getIdlingResource();
        prepareAndLoadList();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPES_LIST_EXTRA, (ArrayList<Recipe>)mRecipes);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    private void prepareAndLoadList() {
        RecipeService client = new RetrofitClient().getClient().create(RecipeService.class);
        Call<List<Recipe>> call = client.get_recipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.d(TAG, "Got recipe data");
                mRecipes = response.body();
                loadRecipeListUI();
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "Error in retrofit");
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle(getString(R.string.retry_title));
                dialog.setMessage(getString(R.string.retry_message));
                dialog.setPositiveButton(getString(R.string.retry_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        prepareAndLoadList();
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }

    public void loadRecipeListUI () {
        //test idling resource
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecipesAdapter recipesAdapter = new RecipesAdapter(this, this, mRecipes);
        RecyclerView recyclerView = findViewById(R.id.recipe_rv);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recipesAdapter);
    }

    @Override
    public void onRecipeItemClicked(int position) {
        Recipe item = mRecipes.get(position);
        Toast.makeText(getApplicationContext(),"Item Selected position "+ position, Toast.LENGTH_SHORT).show();
        Intent detailRecipeListIntent = new Intent(MainActivity.this, RecipeListItemDetailsActivity.class);
        detailRecipeListIntent.putExtra(RecipeListItemDetailsActivity.RECIPE_EXTRA, item);

        WidgetUpdateService.startActionUpdateListView(getApplicationContext(), item);
        startActivity(detailRecipeListIntent);
    }
}
