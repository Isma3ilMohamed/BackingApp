package com.udacity.backingapp;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;


import android.os.Bundle;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.udacity.backingapp.ui.activity.MainRecipesList;
import com.udacity.backingapp.ui.fragment.RecipeFragment;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by The Dev Wolf on 29-03-2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainListTest {

    @Rule
    public ActivityTestRule<MainRecipesList> mainRecipesListActivityTestRule
            = new ActivityTestRule<MainRecipesList>(MainRecipesList.class, true, true);


    @Test
    public void checkFragmentContainer() {


        onView(withId(R.id.mainContainer))
                .perform(click())
                .check(matches(isDisplayed()));


    }

    @Test
    public void scrollToPosition() {

        onView(withId(R.id.mainContainer))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(withId(R.id.recipe_container))
                .perform(click())
                .check(matches(isDisplayed()));
        /*onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));*/

    }


}
