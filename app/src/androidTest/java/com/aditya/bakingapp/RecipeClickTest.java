package com.aditya.bakingapp;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aditya.bakingapp.home.MainActivity;
import com.aditya.bakingapp.recipe.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeClickTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeItem() {
        onView(withId(R.id.list)).perform(actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeActivity.class.getName()));
    }
}
