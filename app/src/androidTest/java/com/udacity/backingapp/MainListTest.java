package com.udacity.backingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.backingapp.ui.activity.MainRecipesList;

import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Created by The Dev Wolf on 29-03-2018.
 */
@RunWith(AndroidJUnit4.class)
class MainListTest {
    @Rule
    public ActivityTestRule<MainRecipesList> mainRecipesListActivityTestRule
            = new ActivityTestRule<MainRecipesList>(MainRecipesList.class);
}
