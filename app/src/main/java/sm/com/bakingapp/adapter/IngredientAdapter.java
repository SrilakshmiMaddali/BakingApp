package sm.com.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sm.com.bakingapp.Ingredient;
import sm.com.bakingapp.R;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientItemViewHolder> {

    Context mContext;
    List<Ingredient> mItems;

    public IngredientAdapter(Context mContext, List<Ingredient> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    public class IngredientItemViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        TextView recipeAmount;

        public IngredientItemViewHolder(View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.ingredient_name_tv);
            recipeAmount = itemView.findViewById(R.id.ingredient_amount_tv);
        }

        public void setData(Ingredient data) {
            recipeName.setText(data.getIngredient());
            recipeAmount.setText(mContext.getResources().getString(R.string.ingredient_amount, data.getQuantity(), data.getMeasure()));
        }
    }

    @NonNull
    @Override
    public IngredientItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredients_view,parent,false);
        return new IngredientItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientItemViewHolder holder, int position) {
        holder.setData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
