package sm.com.bakingapp.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import sm.com.bakingapp.R;
import sm.com.bakingapp.Recipe;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    Context mContext;
    protected static ItemClickListener mItemClickListener;
    public List<Recipe> mData;
    int[] drawableIds = {R.drawable.nutellapie, R.drawable.brownie, R.drawable.yellowck, R.drawable.cheesecake};

    public RecipesAdapter(Context mContext, ItemClickListener mItemClickListener, List<Recipe> mData) {
        this.mContext = mContext;
        this.mItemClickListener = mItemClickListener;
        this.mData = mData;
    }

    public interface ItemClickListener {
        void onRecipeItemClicked(int position);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipte_list_item_view, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        String imagePath = mData.get(position).getImage();
        if ( imagePath != null && !imagePath.isEmpty()) {
            loadImage(imagePath, holder.recipeIv);
        } else {
            mData.get(position).setImageId(drawableIds[position]);
        }
        holder.setData(mData.get(position), position);
    }

    private void loadImage(String path, ImageView imageView) {
        Picasso.with(mContext)
                .load(path)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // image loading finished, so remove progressbar.
                    }

                    @Override
                    public void onError() {
                        // On error , while loading sandwich image, display error message.
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipeIv;
        TextView recipeTv;
        int posVh;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            recipeIv = itemView.findViewById(R.id.recipe_iv);
            recipeTv = itemView.findViewById(R.id.recipe_tv);
        }

        public void setData(Recipe data, int pos) {
            recipeTv.setText(data.getName());
            recipeIv.setImageResource(data.getImageId());
            posVh = pos;
        }

        @Override
        public void onClick(View v) {
            RecipesAdapter.mItemClickListener.onRecipeItemClicked(posVh);
        }
    }
}