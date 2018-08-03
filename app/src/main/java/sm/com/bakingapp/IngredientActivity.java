package sm.com.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

import sm.com.bakingapp.util.IngredientFragment;

public class IngredientActivity extends AppCompatActivity {
    public static String INGREDIENTS_EXTRA = "i_extra";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_activity);

        Intent intent =  getIntent();
        if (intent == null) {
            closeOnError();
        }

        List<Ingredient> mIngredients = intent.getParcelableArrayListExtra(INGREDIENTS_EXTRA);
        FragmentManager fragmentManager = getSupportFragmentManager();
        IngredientFragment ingredientFragment = new IngredientFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IngredientFragment.INGREDIENTS_EXTRA, (ArrayList<? extends Parcelable>) mIngredients);
        ingredientFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_fragment,ingredientFragment)
                .commit();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, getString(R.string.close_on_intent_error), Toast.LENGTH_SHORT).show();
    }

}
