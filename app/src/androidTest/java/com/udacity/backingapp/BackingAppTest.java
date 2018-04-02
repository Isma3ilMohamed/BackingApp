package com.udacity.backingapp;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.udacity.backingapp.ui.activity.DetailRecipe;
import com.udacity.backingapp.ui.activity.MainRecipesList;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by The Dev Wolf on 29-03-2018.
 */
@RunWith(AndroidJUnit4.class)
public class BackingAppTest {

    @Rule
    public IntentsTestRule<MainRecipesList> mainRecipesListActivityTestRule
            = new IntentsTestRule<MainRecipesList>(MainRecipesList.class);


    /*
    * check if main container is displayed
    * */
    @Test
    public void checkFragmentContainer() {

        onView(withId(R.id.mainContainer))
                .perform(click())
                .check(matches(isDisplayed()));

    }

    /*
    * check if recycler view is swiped  items is clicked
    * */
    @Test
    public void testRecyclerViewMoveToSpecificPosition() {
        onView(withId(R.id.mainContainer))
                .perform(click());

        //takeSecond();
        onView(allOf(withId(R.id.rv_recipe_list), isDisplayed())).perform(swipeUp());

        onView(allOf(withId(R.id.rv_recipe_list)))
                //you can select position from 0 to 3
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


    }


    /*
    * check if detail recipe activity is started
    * */
    @Test
    public void testDetailActivityIsLaunched() {
        testRecyclerViewMoveToSpecificPosition();
        intended(hasComponent(DetailRecipe.class.getName()));


    }

    /*
    * check if EXO player is displayed and work well
    * */

    @Test
    public void testExoPlayer() {
        testRecyclerViewMoveToSpecificPosition();

        onView(withId(R.id.player_view))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.player_view),
                withClassName(is(SimpleExoPlayerView.class.getName()))));

    }


    /*
    * test steps recycler view
    * */
    @Test
    public void testStepsRecyclerView() {

        testRecyclerViewMoveToSpecificPosition();
        onView(withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

    }


    /*
    * test ingredients recycler view
    * */
    @Test
    public void testIngredientsRecyclerView() {
        testRecyclerViewMoveToSpecificPosition();
        onView(withId(R.id.stepDetailContainer));

        onView(withId(R.id.rv_ingredients))
                .perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

    }

    /*
    * test if  main description text and
    * detail description is not empty
    * */

    @Test
    public void testStepDescriptionsText() {

        testStepsRecyclerView();
        // passes if the textView does not match the empty string
        onView(withId(R.id.tv_main_desc)).check(matches(not(withText(""))));
        onView(withId(R.id.tv_detail_desc)).check(matches(not(withText(""))));

    }



}
