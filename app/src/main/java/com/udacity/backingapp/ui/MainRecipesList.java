package com.udacity.backingapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.udacity.backingapp.R;
import com.udacity.backingapp.api.RecipeApi;
import com.udacity.backingapp.api.RetrofitCall;
import com.udacity.backingapp.databinding.MainRecipesListBinding;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.utils.ConnectionUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainRecipesList extends AppCompatActivity {
    public static final String RECIPES = "recipes";
    private RecipeApi service;
    private List<Recipe> recipeList;
    private MainRecipesListBinding mMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.main_recipes_list);

        recipeList = new ArrayList<>();
        getRecipes();

    }


    void getRecipes() {
        if (ConnectionUtils.checkConnection(this)) {
            service = RetrofitCall.getRecipes().create(RecipeApi.class);
            Observable<List<Recipe>> observable = service.getRecipes();
            observable.observeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Recipe>>() {
                        @Override
                        public void call(List<Recipe> recipes) {
                            recipeList = recipes;
                            //Toast.makeText(getApplicationContext(), recipes.get(2).getName(), Toast.LENGTH_LONG).show();
                            launchRecipeFragment(recipes);
                        }

                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    void launchRecipeFragment(List<Recipe> recipes) {

        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(RECIPES, (ArrayList<? extends Parcelable>) recipes);
        recipeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(mMainBinding.container.getId(), recipeFragment)
                .commit();

    }
}
