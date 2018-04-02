package com.udacity.backingapp;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.udacity.backingapp.ui.activity.MainRecipesList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by The Dev Wolf on 02-04-2018.
 */

public class BackingAppTabletTest {

    @Rule
    public IntentsTestRule<MainRecipesList> mainRecipesListActivityTestRule
            = new IntentsTestRule<MainRecipesList>(MainRecipesList.class, false,
            true);


    @Before
    public void setUp() throws Exception {

        mainRecipesListActivityTestRule.getActivity().
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }


    /*
    * check if main container is displayed
    * */
    @Test
    public void checkFragmentContainer() {

        onView(withId(R.id.mainContainer))
                .perform(click())
                .check(matches(isDisplayed()));

    }

    @Test
    public void testRecyclerViewMoveToSpecificPosition() {

        checkFragmentContainer();
        //takeSecond();
        onView(withId(R.id.rv_recipe_list)).perform(swipeUp())
                //you can select position from 0 to 3
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));


    }

    @Test
    public void testExoPlayer() {
        testRecyclerViewMoveToSpecificPosition();
        onView(withId(R.id.stepDetailContainer));
        onView(withId(R.id.player_view))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.player_view),
                withClassName(is(SimpleExoPlayerView.class.getName()))));
    }

    @Test
    public void testStepRecyclerView() {
        testRecyclerViewMoveToSpecificPosition();

        onView(withId(R.id.sw_detail_frame));
        onView(withId(R.id.stepDetailContainer));
        onView(withId(R.id.scroll_step))
                .perform(swipeFromTopToBottom());

        onView(withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
    }


    @Test
    public void testIngredientsRecyclerView() {
        testRecyclerViewMoveToSpecificPosition();

        onView(withId(R.id.sw_detail_frame));
        onView(withId(R.id.stepDetailContainer));
        onView(withId(R.id.scroll_step))
                .perform(swipeFromTopToBottom());

        onView(withId(R.id.rv_ingredients))
                .perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
    }

    @Test
    public void testStepDescriptionsText() {
        testRecyclerViewMoveToSpecificPosition();
        onView(withId(R.id.tv_main_desc)).check(matches(not(withText(""))));
        onView(withId(R.id.tv_detail_desc)).check(matches(not(withText(""))));
    }


    //method for make swipe up for scrollview
    private static ViewAction swipeFromTopToBottom() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER,
                GeneralLocation.TOP_CENTER, Press.FINGER);

    }
}
