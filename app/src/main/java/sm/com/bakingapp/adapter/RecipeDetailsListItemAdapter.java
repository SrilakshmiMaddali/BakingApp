package sm.com.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sm.com.bakingapp.R;

public class RecipeDetailsListItemAdapter extends RecyclerView.Adapter<RecipeDetailsListItemAdapter.ListItemHolder> {

    Context mContext;
    ArrayList<String> mData;
    protected static ItemListener mItemClickListener;

    public RecipeDetailsListItemAdapter(Context mContext, ItemListener mItemClickListener, ArrayList<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.menu_item_layout, parent, false);
        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        holder.setData(mData.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface ItemListener {
        void onItemSelected(int pos);
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView shortDescription;
        int index;

        public ListItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            shortDescription = itemView.findViewById(R.id.short_description_tv);
        }

        public void setData (String data, int pos){
            shortDescription.setText(data);
            index = pos;
        }

        @Override
        public void onClick(View v) {
            RecipeDetailsListItemAdapter.mItemClickListener.onItemSelected(index);
        }
    }
}