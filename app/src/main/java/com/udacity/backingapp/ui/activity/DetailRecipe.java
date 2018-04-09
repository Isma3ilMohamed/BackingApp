package com.udacity.backingapp.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.backingapp.R;
import com.udacity.backingapp.adapter.IngredientAdapter;
import com.udacity.backingapp.adapter.StepAdapter;
import com.udacity.backingapp.databinding.ActivityDetailRecipeBinding;
import com.udacity.backingapp.model.Ingredients;
import com.udacity.backingapp.model.Steps;
import com.udacity.backingapp.test.mIdingResource;
import com.udacity.backingapp.ui.fragment.DetailRecipeFragment;
import com.udacity.backingapp.ui.fragment.StepFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailRecipe extends AppCompatActivity implements StepAdapter.StepListener {
    @Nullable
    private mIdingResource idingResource;

    //Data Binding Declaration
    ActivityDetailRecipeBinding mDetailRecipeBinding;


    DetailRecipeFragment fragment;
    //Ingredients & Steps Data Source
    List<Steps> stepsList;
    List<Ingredients> ingredientsList;
    //Layout Managers
    GridLayoutManager ingredientsManager;
    GridLayoutManager stepManager;

    //Step Fragment
    StepFragment stepFragment;

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

    public static final String INGREDIENTS = "ingredients";
    public static final String STEPS = "steps";
    public static final String TITLE = "title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIdlingResource();

        if (idingResource != null) {
            idingResource.setIdleState(false);
        }
        mDetailRecipeBinding = DataBindingUtil.setContentView
                (this, R.layout.activity_detail_recipe);


        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.


        if (mDetailRecipeBinding.stepsListContainer != null) {
            ingredientsList = new ArrayList<>();
            stepsList = new ArrayList<>();
            if (getIntent().getExtras() != null) {
                ingredientsList = getIntent().getParcelableArrayListExtra(INGREDIENTS);
                stepsList = getIntent().getParcelableArrayListExtra(STEPS);
                setActionBarTitle(getIntent().getStringExtra(TITLE));

            }
            //tablet init
            tabletInit();


        } else {
            if (savedInstanceState != null) {
                return;
            }
            ingredientsList = new ArrayList<>();
            stepsList = new ArrayList<>();
            if (getIntent().getExtras() != null) {
                ingredientsList = getIntent().getParcelableArrayListExtra(INGREDIENTS);
                stepsList = getIntent().getParcelableArrayListExtra(STEPS);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getIntent().getStringExtra(TITLE));
                }

            }


            fragment = new DetailRecipeFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(INGREDIENTS, (ArrayList<? extends Parcelable>) ingredientsList);
            bundle.putParcelableArrayList(STEPS, (ArrayList<? extends Parcelable>) stepsList);
            bundle.putString(TITLE, getIntent().getStringExtra(TITLE));
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(mDetailRecipeBinding.detailRecipeContainer.getId(), fragment)
                    .addToBackStack(null)
                    .commit();
        }


    }

    private void tabletInit() {
        //Layout Manager
        ingredientsManager = new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        stepManager = new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.VERTICAL, false);
        //add layout managers to recyclerview
        mDetailRecipeBinding.stepsListContainer.setLayoutManager(stepManager);
        mDetailRecipeBinding.ingredientsListContainer.setLayoutManager(ingredientsManager);
        stepAdapter = new StepAdapter(stepsList, this);
        if (stepAdapter != null) {
            if (idingResource != null) {
                idingResource.setIdleState(false);
            }
        }
        mDetailRecipeBinding.stepsListContainer.setAdapter(stepAdapter);


        ingredientAdapter = new IngredientAdapter(ingredientsList, getApplicationContext());
        if (ingredientAdapter != null) {
            if (idingResource != null) {
                idingResource.setIdleState(false);
            }
        }
        mDetailRecipeBinding.ingredientsListContainer.setAdapter(ingredientAdapter);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mDetailRecipeBinding.stepsListContainer != null) {
            mBundleSaveState = new Bundle();
            ingredientsRecyclerStateParcelable = mDetailRecipeBinding.ingredientsListContainer.
                    getLayoutManager().onSaveInstanceState();
            stepssRecyclerStateParcelable = mDetailRecipeBinding.stepsListContainer.
                    getLayoutManager().onSaveInstanceState();
            mBundleSaveState.putParcelable(ingredientStateKey, ingredientsRecyclerStateParcelable);

            mBundleSaveState.putParcelable(stepStateKey, stepssRecyclerStateParcelable);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDetailRecipeBinding.stepsListContainer != null) {

            if (mBundleSaveState != null) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        ingredientsRecyclerStateParcelable = mBundleSaveState.getParcelable(ingredientStateKey);
                        mDetailRecipeBinding.ingredientsListContainer.getLayoutManager()
                                .onRestoreInstanceState(ingredientsRecyclerStateParcelable);
                        stepssRecyclerStateParcelable = mBundleSaveState.getParcelable(stepStateKey);
                        mDetailRecipeBinding.stepsListContainer.getLayoutManager()
                                .onRestoreInstanceState(stepssRecyclerStateParcelable);
                    }
                }, 50);
            }

            mDetailRecipeBinding.stepsListContainer.setLayoutManager(stepManager);
            mDetailRecipeBinding.ingredientsListContainer.setLayoutManager(ingredientsManager);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStackImmediate();
        startActivity(new Intent(DetailRecipe.this, MainRecipesList.class));

    }

    @Override
    public void onStepClickListener(Steps step) {
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SINGLE_STEP, step);
        stepFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(mDetailRecipeBinding.stepDetailContainer.getId(), stepFragment)
                .commit();
    }


    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
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
