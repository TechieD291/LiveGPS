package com.gonext.livegps.routenavigation.adpter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.gonext.livegps.routenavigation.R;
import com.gonext.livegps.routenavigation.models.CategoryModelClass;

import java.util.ArrayList;


public class CategoryListRecyclerViewAdpter extends RecyclerView.Adapter<CategoryListRecyclerViewAdpter.ViewHolder> {
    ArrayList<CategoryModelClass> CategoryArrayList;
    Activity activity;
    Clickable clickable;

    public interface Clickable {
        void onclick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item isIcon just a string in this case
        public View categoryView;
        public TextView tvCategoryName;
        ImageView ivIcon;

        public ViewHolder(View v) {
            super(v);
            categoryView = v;
            tvCategoryName = (TextView) v.findViewById(R.id.name);
            ivIcon = (ImageView) v.findViewById(R.id.icon);
        }
    }

    public CategoryListRecyclerViewAdpter(ArrayList<CategoryModelClass> cancelledTrainArrayList, Activity activity, Clickable clickable) {
        this.CategoryArrayList = cancelledTrainArrayList;
        this.activity = activity;
        this.clickable = clickable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_row, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CategoryModelClass modelClass = CategoryArrayList.get(position);
        holder.tvCategoryName.setText(modelClass.getCategoryName());
        Resources resources = activity.getResources();
        Log.e("test", modelClass.getCategoryIcon());
        final int resourceId = resources.getIdentifier(modelClass.getCategoryIcon(), "drawable",
                activity.getPackageName());
        holder.ivIcon.setImageDrawable(resources.getDrawable(resourceId));
        holder.categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickable.onclick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CategoryArrayList.size();
    }


}

