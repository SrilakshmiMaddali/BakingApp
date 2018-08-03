package sm.com.bakingapp.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import sm.com.bakingapp.R;
import sm.com.bakingapp.adapter.RecipeDetailsListItemAdapter;

public class RecipeListItemDetailsFragment extends Fragment {

    public static String LIST_NAMES_EXTRA = "names_extra";

    private ArrayList<String> mNameList;

    public RecipeListItemDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mNameList = savedInstanceState.getStringArrayList(LIST_NAMES_EXTRA);
        } else {
            mNameList = getArguments().getStringArrayList(LIST_NAMES_EXTRA);
        }

        final View rootView = inflater.inflate(R.layout.recipe_details_fragment,container,false);

        RecyclerView recyclerView = rootView.findViewById(R.id.lv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecipeDetailsListItemAdapter recipeDetailsListItemAdapter = new RecipeDetailsListItemAdapter(getContext(),(RecipeDetailsListItemAdapter.ItemListener) getContext(), mNameList);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recipeDetailsListItemAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(LIST_NAMES_EXTRA,mNameList);
    }
}
