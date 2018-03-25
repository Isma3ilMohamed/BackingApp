package com.udacity.backingapp.ui;


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

import com.udacity.backingapp.R;
import com.udacity.backingapp.adapter.IngredientAdapter;
import com.udacity.backingapp.adapter.StepAdapter;
import com.udacity.backingapp.databinding.FragmentDetailRecipeBinding;
import com.udacity.backingapp.model.Ingredients;
import com.udacity.backingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailRecipeFragment extends Fragment implements StepAdapter.StepListener {

    //Data Binding Declaration
    FragmentDetailRecipeBinding mFragmentDetailRecipeBinding;
    //Ingredients & Steps Data Source
    List<Steps> stepsList;
    List<Ingredients> ingredientsList;
    //Layout Managers
    GridLayoutManager ingredientsManager;
    GridLayoutManager stepManager;

    //Step Fragment
    StepFragment stepFragment;

    //Adapters
    StepAdapter stepAdapter;
    IngredientAdapter ingredientAdapter;


    private final String ingredientStateKey = "ingredient_state";
    private final String stepStateKey = "step_state";
    //declare Parcelable to store recycler view state
    private Parcelable ingredientsRecyclerStateParcelable = null;
    private Parcelable stepssRecyclerStateParcelable = null;
    //declare bundle to store recycler view state key & parcelable
    private static Bundle mBundleSaveState;

    public DetailRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentDetailRecipeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_recipe, container, false);
        //Layout Manager
        ingredientsManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        stepManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        mFragmentDetailRecipeBinding.rvIngredients.setLayoutManager(ingredientsManager);
        mFragmentDetailRecipeBinding.rvSteps.setLayoutManager(stepManager);


        stepsList = new ArrayList<>();
        ingredientsList = new ArrayList<>();

        if (this.getArguments() != null) {
            ingredientsList = this.getArguments().getParcelableArrayList("ingredients");
            stepsList = this.getArguments().getParcelableArrayList("steps");

        }


        stepAdapter = new StepAdapter(stepsList, getContext(), this);
        mFragmentDetailRecipeBinding.rvSteps.setAdapter(stepAdapter);

        ingredientAdapter = new IngredientAdapter(ingredientsList, getContext());
        mFragmentDetailRecipeBinding.rvIngredients.setAdapter(ingredientAdapter);

        //Start first step
        stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("step", stepsList.get(0));
        stepFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(mFragmentDetailRecipeBinding.stepDetailContainer.getId(), stepFragment)
                .commit();


        return mFragmentDetailRecipeBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBundleSaveState = new Bundle();
        ingredientsRecyclerStateParcelable = mFragmentDetailRecipeBinding.rvIngredients.
                getLayoutManager().onSaveInstanceState();
        stepssRecyclerStateParcelable = mFragmentDetailRecipeBinding.rvSteps.
                getLayoutManager().onSaveInstanceState();
        mBundleSaveState.putParcelable(ingredientStateKey, ingredientsRecyclerStateParcelable);
        //getFragmentManager().putFragment(mBundleSaveState,"Step",s);

        mBundleSaveState.putParcelable(stepStateKey, stepssRecyclerStateParcelable);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mBundleSaveState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    ingredientsRecyclerStateParcelable = mBundleSaveState.getParcelable(ingredientStateKey);
                    mFragmentDetailRecipeBinding.rvIngredients.getLayoutManager().onRestoreInstanceState(ingredientsRecyclerStateParcelable);
                    stepssRecyclerStateParcelable = mBundleSaveState.getParcelable(stepStateKey);
                    mFragmentDetailRecipeBinding.rvSteps.getLayoutManager().onRestoreInstanceState(stepssRecyclerStateParcelable);
                }
            }, 50);
        }

        mFragmentDetailRecipeBinding.rvSteps.setLayoutManager(stepManager);
        mFragmentDetailRecipeBinding.rvIngredients.setLayoutManager(ingredientsManager);
    }


    @Override
    public void onStepClickListener(Steps step) {

        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        stepFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(mFragmentDetailRecipeBinding.stepDetailContainer.getId(), stepFragment)
                .commit();
    }


}
