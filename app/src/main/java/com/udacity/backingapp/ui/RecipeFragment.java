package com.udacity.backingapp.ui;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.udacity.backingapp.R;
import com.udacity.backingapp.adapter.RecipeAdapter;
import com.udacity.backingapp.databinding.FragmentRecipeBinding;
import com.udacity.backingapp.model.Ingredients;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeListener {
    //use dataBinding on fragment
    FragmentRecipeBinding mRecipeBinding;
    //make recipe list as a recyclerview data source
    List<Recipe> recipeList;
    List<Ingredients> ingredientsList;
    List<Steps> stepsList;
    //layout manager
    GridLayoutManager layoutManager;
    //make recipe adapter
    RecipeAdapter adapter;

    Boolean mTwoBane;


    //save instance for recycler view
    private final String recyclerStateKey = "recipe_state";
    //declare Parcelable to store recycler view state
    private Parcelable recyclerViewStateParcelable = null;

    //declare bundle to store recycler view state key & parcelable
    private static Bundle mBundleRecyclerViewState;

    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRecipeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe, container,
                false);

        if (mRecipeBinding.swDetailFrame != null) {
            mTwoBane = true;
            layoutManager = new GridLayoutManager(getContext(), 1,
                    LinearLayoutManager.HORIZONTAL, false);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {
            mTwoBane = false;
            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = new GridLayoutManager(getContext(), 1);
            } else if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = new GridLayoutManager(getContext(), 2);
            }
        }


        mRecipeBinding.rvRecipeList.setLayoutManager(layoutManager);

        Bundle bundle = this.getArguments();
        recipeList = bundle.getParcelableArrayList("recipes");
        adapter = new RecipeAdapter(getContext(), recipeList, this);
        mRecipeBinding.rvRecipeList.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down));
        mRecipeBinding.rvRecipeList.scheduleLayoutAnimation();
        mRecipeBinding.rvRecipeList.setAdapter(adapter);


        return mRecipeBinding.getRoot();
    }


    @Override
    public void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        recyclerViewStateParcelable = mRecipeBinding.rvRecipeList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(recyclerStateKey, recyclerViewStateParcelable);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    recyclerViewStateParcelable = mBundleRecyclerViewState.getParcelable(recyclerStateKey);
                    mRecipeBinding.rvRecipeList.getLayoutManager().onRestoreInstanceState(recyclerViewStateParcelable);

                }
            }, 40);
        }


        mRecipeBinding.rvRecipeList.setLayoutManager(layoutManager);

    }

    @Override
    public void onRecipeClickListener(Recipe recipe) {
        if (mTwoBane) {
            ingredientsList = new ArrayList<>();
            stepsList = new ArrayList<>();

            ingredientsList = recipe.getIngredients();
            stepsList = recipe.getSteps();
            DetailRecipeFragment fragment = new DetailRecipeFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) ingredientsList);
            bundle.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) stepsList);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.sw_detail_frame, fragment)
                    .commit();

        } else {
            ingredientsList = new ArrayList<>();
            stepsList = new ArrayList<>();

            ingredientsList = recipe.getIngredients();
            stepsList = recipe.getSteps();

            Intent intent = new Intent(getActivity(), DetailRecipe.class);
            intent.putParcelableArrayListExtra("ingredients", (ArrayList<? extends Parcelable>) ingredientsList);
            intent.putParcelableArrayListExtra("steps", (ArrayList<? extends Parcelable>) stepsList);
            intent.putExtra("title", recipe.getName());
            startActivity(intent);
        }
    }
}
