package com.udacity.backingapp.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.backingapp.R;
import com.udacity.backingapp.databinding.ActivityDetailRecipeBinding;
import com.udacity.backingapp.model.Ingredients;
import com.udacity.backingapp.model.Steps;
import com.udacity.backingapp.ui.fragment.DetailRecipeFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailRecipe extends AppCompatActivity {

    //Data Binding Declaration
    ActivityDetailRecipeBinding mDetailRecipeBinding;

    //Steps ,  Ingredients Data Sources
    List<Ingredients> ingredientsList;
    List<Steps> stepsList;
    DetailRecipeFragment fragment;

    public static final String INGREDIENTS = "ingredients";
    public static final String STEPS = "steps";
    public static final String TITLE = "title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailRecipeBinding = DataBindingUtil.setContentView
                (this, R.layout.activity_detail_recipe);

        onRetainNonConfigurationInstance();

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
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
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(mDetailRecipeBinding.detailContainer.getId(), fragment)
                .addToBackStack(null)
                .commit();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStackImmediate();
        startActivity(new Intent(DetailRecipe.this, MainRecipesList.class));
    }
}
