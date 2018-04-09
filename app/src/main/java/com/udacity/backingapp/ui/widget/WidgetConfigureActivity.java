package com.udacity.backingapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.animation.AnimationUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.udacity.backingapp.R;
import com.udacity.backingapp.adapter.RecipeAdapter;
import com.udacity.backingapp.api.RecipeApi;
import com.udacity.backingapp.api.RetrofitCall;
import com.udacity.backingapp.databinding.ActivityWidgetConfigureBinding;
import com.udacity.backingapp.model.Ingredients;
import com.udacity.backingapp.model.Recipe;
import com.udacity.backingapp.utils.ConnectionUtils;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WidgetConfigureActivity extends AppCompatActivity implements RecipeAdapter.RecipeListener {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RecipeApi service;
    private AppWidgetManager widgetManager;
    private RemoteViews views;

    //RecyclerView Config
    GridLayoutManager layoutManager;
    //make recipe adapter
    RecipeAdapter adapter;


    //save instance for recycler view
    private final String recyclerStateKey = "recipe_state";
    private Parcelable recyclerViewStateParcelable = null;
    private static Bundle mBundleRecyclerViewState;

    ActivityWidgetConfigureBinding mConfigureBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        mConfigureBinding = DataBindingUtil.setContentView(this, R.layout.activity_widget_configure);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.widget_activity_title));
        }

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        } else if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        }


        mConfigureBinding.rvSelectRecipe.setLayoutManager(layoutManager);


        getRecipes();


        // These steps are seen in the previous examples
        widgetManager = AppWidgetManager.getInstance(this);
        views = new RemoteViews(this.getPackageName(), R.layout.recipe_widget);
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

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

                            //Add Data Source to RecyclerView
                            adapter = new RecipeAdapter(getApplicationContext(), recipes,
                                    WidgetConfigureActivity.this);
                            mConfigureBinding.rvSelectRecipe.setLayoutAnimation(
                                    AnimationUtils.loadLayoutAnimation(getApplicationContext(),
                                            R.anim.layout_animation_fall_down));
                            mConfigureBinding.rvSelectRecipe.scheduleLayoutAnimation();
                            mConfigureBinding.rvSelectRecipe.setAdapter(adapter);
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

    @Override
    protected void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        recyclerViewStateParcelable = mConfigureBinding.rvSelectRecipe.getLayoutManager().onSaveInstanceState();
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
                    mConfigureBinding.rvSelectRecipe.getLayoutManager().onRestoreInstanceState(recyclerViewStateParcelable);

                }
            }, 40);
        }


        mConfigureBinding.rvSelectRecipe.setLayoutManager(layoutManager);
    }

    @Override
    public void onRecipeClickListener(Recipe recipe) {
        //views.setOnClickPendingIntent(R.id.appwidget_text, pending);
        views.setTextViewText(R.id.tv_widget_recipe_name, recipe.getName());
        List<Ingredients> ingredientsList = recipe.getIngredients();
        String ingredient = "";
        for (int i = 0; i < ingredientsList.size(); i++) {
            ingredient += i + 1 + "-" + ingredientsList.get(i).getIngredient() +
                    " " + ingredientsList.get(i).getMeasure() +
                    " " + ingredientsList.get(i).getQuantity() +
                    "." + "\n \n";
        }
        views.setTextViewText(R.id.tv_widget_ingredient, ingredient);

        widgetManager.updateAppWidget(mAppWidgetId, views);
        Intent resultValue = new Intent();
        // Set the results as expected from a 'configure activity'.
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
