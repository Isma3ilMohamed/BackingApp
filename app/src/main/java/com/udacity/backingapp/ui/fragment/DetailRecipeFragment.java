package com.udacity.backingapp.ui.fragment;


import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
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
import com.udacity.backingapp.test.mIdingResource;
import com.udacity.backingapp.ui.activity.DetailRecipe;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailRecipeFragment extends Fragment implements StepAdapter.StepListener {
    @Nullable
    private mIdingResource idingResource;

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

    public static final String INGREDIENTS = "ingredients";
    public static final String STEPS = "steps";
    public static final String SINGLE_STEP = "step";



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

        getIdlingResource();

        if (idingResource != null) {
            idingResource.setIdleState(false);
        }

        //Layout Manager
        ingredientsManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        stepManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        mFragmentDetailRecipeBinding.rvIngredients.setLayoutManager(ingredientsManager);
        mFragmentDetailRecipeBinding.rvSteps.setLayoutManager(stepManager);


        stepsList = new ArrayList<>();
        ingredientsList = new ArrayList<>();

        if (this.getArguments() != null) {
            ingredientsList = this.getArguments().getParcelableArrayList(INGREDIENTS);
            stepsList = this.getArguments().getParcelableArrayList(STEPS);

        }


        stepAdapter = new StepAdapter(stepsList, getContext(), this);
        if (stepAdapter != null) {
            if (idingResource != null) {
                idingResource.setIdleState(false);
            }
        }
        mFragmentDetailRecipeBinding.rvSteps.setAdapter(stepAdapter);


        ingredientAdapter = new IngredientAdapter(ingredientsList, getContext());
        if (ingredientAdapter != null) {
            if (idingResource != null) {
                idingResource.setIdleState(false);
            }
        }
        mFragmentDetailRecipeBinding.rvIngredients.setAdapter(ingredientAdapter);

        //Start first step
        stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        if (stepsList != null) {
            bundle.putParcelable(SINGLE_STEP, stepsList.get(0));
            stepFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(mFragmentDetailRecipeBinding.stepDetailContainer.getId(), stepFragment)
                    .commit();

        }
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
        bundle.putParcelable(SINGLE_STEP, step);
        stepFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(mFragmentDetailRecipeBinding.stepDetailContainer.getId(), stepFragment)
                .commit();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idingResource == null) {
            idingResource = new mIdingResource();
        }
        return idingResource;
    }

}
