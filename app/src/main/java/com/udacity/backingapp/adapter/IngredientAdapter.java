package com.udacity.backingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Ingredients;

import java.util.List;

/**
 * Created by The Dev Wolf on 21-03-2018.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private List<Ingredients> ingredientsList;
    private Context mContext;

    public IngredientAdapter(List<Ingredients> ingredientsList, Context mContext) {
        this.ingredientsList = ingredientsList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ingredients ingredient = ingredientsList.get(position);
        holder.tv_ingredient_item.setText(ingredient.getIngredient());
        holder.tv_measure_item.setText(ingredient.getMeasure());
        holder.tv_quantity_item.setText(String.valueOf(ingredient.getQuantity()));

    }


    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ingredient_item;
        TextView tv_quantity_item;
        TextView tv_measure_item;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_ingredient_item = itemView.findViewById(R.id.tv_ingreient_item);
            tv_quantity_item = itemView.findViewById(R.id.tv_quantity_item);
            tv_measure_item = itemView.findViewById(R.id.tv_measure_item);
        }
    }
}
