package com.udacity.backingapp.ui.activity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.backingapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by The Dev Wolf on 31-03-2018.
 */
@RunWith(AndroidJUnit4.class)
public class DetailRecipeTest {
    @Rule
    public ActivityTestRule<DetailRecipe> detailRecipeActivityTestRule =
            new ActivityTestRule<DetailRecipe>(DetailRecipe.class);


    @Before
    public void init() {
        detailRecipeActivityTestRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction();
    }

    @Test
    public void testDetailContainer() {
        onView(withId(R.id.stepDetailContainer))
                .perform(click());
    }


}