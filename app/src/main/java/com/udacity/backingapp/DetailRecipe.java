package com.udacity.backingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.udacity.backingapp.adapter.IngredientAdapter;
import com.udacity.backingapp.databinding.ActivityDetailRecipeBinding;
import com.udacity.backingapp.model.Ingredients;
import com.udacity.backingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

public class DetailRecipe extends AppCompatActivity {

    //Data Binding Declaration
    ActivityDetailRecipeBinding mDetailRecipeBinding;

    //Steps ,  Ingredients Data Sources
    List<Ingredients> ingredientsList;
    List<Steps> stepsList;
    DetailRecipeFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailRecipeBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_recipe);

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        ingredientsList = new ArrayList<>();
        stepsList = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            ingredientsList = getIntent().getParcelableArrayListExtra("ingredients");
            stepsList = getIntent().getParcelableArrayListExtra("steps");

        }


        fragment = new DetailRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) ingredientsList);
        bundle.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) stepsList);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(mDetailRecipeBinding.detailContainer.getId(), fragment)
                .addToBackStack(null)
                .commit();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DetailRecipe.this, MainRecipesList.class));
    }
}
