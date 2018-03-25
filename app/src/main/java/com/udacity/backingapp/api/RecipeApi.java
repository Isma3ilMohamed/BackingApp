package com.udacity.backingapp.api;

import com.udacity.backingapp.model.Recipe;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by The Dev Wolf on 17-03-2018.
 */

public interface RecipeApi {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Observable<List<Recipe>> getRecipes();

}
