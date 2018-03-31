package com.udacity.backingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Recipe;

import java.util.List;

/**
 * Created by The Dev Wolf on 19-03-2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context mContext;
    private List<Recipe> recipeList;
    private RecipeListener mCallback;

    public static final String NUTTELLA = "Nutella Pie";
    public static final String BROWNIES = "Brownies";
    public static final String YELLOW_CAKE = "Yellow Cake";
    public static final String CHEESE_CAKE = "Cheesecake";

    public RecipeAdapter(Context mContext, List<Recipe> recipeList, RecipeListener mCallback) {
        this.mContext = mContext;
        this.recipeList = recipeList;
        this.mCallback = mCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        setRecipeImage(recipe.getName(), holder.recipeImage);
        holder.recipeTitle.setText(recipe.getName());
        holder.recipeServings.setText(String.valueOf(recipe.getServings()));
    }


    private void setRecipeImage(String name, ImageView recipeImage) {
        if (name.equals(NUTTELLA)) {
            Picasso.with(mContext)
                    .load(R.drawable.nutella)
                    .into(recipeImage);
        } else if (name.equals(BROWNIES)) {
            Picasso.with(mContext)
                    .load(R.drawable.brownies)
                    .into(recipeImage);
        } else if (name.equals(YELLOW_CAKE)) {
            Picasso.with(mContext)
                    .load(R.drawable.yellowcake)
                    .into(recipeImage);
        } else if (name.equals(CHEESE_CAKE)) {
            Picasso.with(mContext)
                    .load(R.drawable.cheesecake)
                    .into(recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipeImage;
        TextView recipeTitle;
        TextView recipeServings;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.iv_recipe_item_image);
            recipeTitle = itemView.findViewById(R.id.tv_recipe_item_title);
            recipeServings = itemView.findViewById(R.id.tv_recipe_item_servangs);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mCallback.onRecipeClickListener(recipeList.get(getAdapterPosition()));
        }
    }


    public interface RecipeListener {
        void onRecipeClickListener(Recipe recipe);
    }

}
