package com.example.lily.lot_pr02_exchange;


import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(
            MainActivity.class);

    // Test initial state.

    @Test
    public void shouldFromEuroBeCheckedInitially() {
        onView(withId(R.id.rbFromEuro)).check(matches(isChecked()));
    }

    @Test
    public void shouldToDollarBeCheckedInitially() {
        onView(withId(R.id.rbToDollar)).check(matches(isChecked()));
    }

    @Test
    public void shouldToEuroBeDisabledInitially() {
        onView(withId(R.id.rbToEuro)).check(matches(not(isEnabled())));
    }

    @Test
    public void shouldFromDollarBeDisabledInitially() {
        onView(withId(R.id.rbToEuro)).check(matches(not(isEnabled())));
    }

    @Test
    public void shouldShowEuroAsFromCurrencyIconInitially() {
        onView(withId(R.id.imgFrom)).check(matches(withTagValue(equalTo(R.drawable.ic_euro))));
    }

    @Test
    public void shouldShowDollarAsToCurrencyIconInitially() {
        onView(withId(R.id.imgTo)).check(matches(withTagValue(equalTo(R.drawable.ic_dollar))));
    }

    @Test
    public void shouldAmountHaveFocusInitially() {
        onView(withId(R.id.txtAmount)).check(matches(hasFocus()));
    }

    @Test
    public void shouldAmountShowZeroInitially() {
        onView(withId(R.id.txtAmount)).check(matches(withText("0.00")));
    }

    @Test
    public void shouldAmountBeSelectedInitially() {
        onView(withId(R.id.txtAmount)).check(matches(hasAllTextSelected()));
    }

    // Test disable same currency.

    @Test
    public void shouldDisableDollarAsToCurrencyWhenDollarSelectedAsFromCurrency() {
        // First change ToCurrency in order to enable FromDollar .
        onView(withId(R.id.rbToPound)).perform(click());
        onView(withId(R.id.rbFromDollar)).perform(click());
        onView(withId(R.id.rbToDollar)).check(matches(not(isEnabled())));
    }

    @Test
    public void shouldDisableEuroAsToCurrencyWhenEuroSelectedAsFromCurrency() {
        onView(withId(R.id.rbFromEuro)).perform(click());
        onView(withId(R.id.rbToEuro)).check(matches(not(isEnabled())));
    }

    @Test
    public void shouldDisablePoundAsToCurrencyWhenPoundSelectedAsFromCurrency() {
        onView(withId(R.id.rbFromPound)).perform(click());
        onView(withId(R.id.rbToPound)).check(matches(not(isEnabled())));
    }

    @Test
    public void shouldDisableDollarAsFromCurrencyWhenDollarSelectedAsFromCurrency() {
        onView(withId(R.id.rbToDollar)).perform(click());
        onView(withId(R.id.rbFromDollar)).check(matches(not(isEnabled())));
    }

    @Test
    public void shouldDisableEuroAsFromCurrencyWhenEuroSelectedAsFromCurrency() {
        // First change FromCurrency in order to enable ToEuro .
        onView(withId(R.id.rbFromPound)).perform(click());
        onView(withId(R.id.rbToEuro)).perform(click());
        onView(withId(R.id.rbFromEuro)).check(matches(not(isEnabled())));
    }

    @Test
    public void shouldDisablePoundAsFromCurrencyWhenPoundSelectedAsFromCurrency() {
        onView(withId(R.id.rbToPound)).perform(click());
        onView(withId(R.id.rbFromPound)).check(matches(not(isEnabled())));
    }

    // Test Show right image on imgTo

    @Test
    public void shouldShowEuroAsFromCurrencyIconWhenRbFromEuroSelected() {
        // Need to change initial state.
        onView(withId(R.id.rbFromPound)).perform(click());
        onView(withId(R.id.rbFromEuro)).perform(click());
        onView(withId(R.id.imgFrom)).check(matches(withTagValue(equalTo(R.drawable.ic_euro))));
    }

    @Test
    public void shouldShowDollarAsFromCurrencyIconWhenRbFromDollarSelected() {
        // Need to change toCurrency first.
        onView(withId(R.id.rbToPound)).perform(click());
        onView(withId(R.id.rbFromDollar)).perform(click());
        onView(withId(R.id.imgFrom)).check(matches(withTagValue(equalTo(R.drawable.ic_dollar))));
    }

    @Test
    public void shouldShowPoundAsFromCurrencyIconWhenRbFromPoundSelected() {
        onView(withId(R.id.rbFromPound)).perform(click());
        onView(withId(R.id.imgFrom)).check(matches(withTagValue(equalTo(R.drawable.ic_pound))));
    }

    // Test Show right image on imgTo

    @Test
    public void shouldShowEuroAsToCurrencyIconWhenRbToEuroSelected() {
        // Need to change fromCurrency first.
        onView(withId(R.id.rbFromPound)).perform(click());
        onView(withId(R.id.rbToEuro)).perform(click());
        onView(withId(R.id.imgTo)).check(matches(withTagValue(equalTo(R.drawable.ic_euro))));
    }

    @Test
    public void shouldShowDollarAsToCurrencyIconWhenRbToDollarSelected() {
        // Need to change initial state.
        onView(withId(R.id.rbToPound)).perform(click());
        onView(withId(R.id.rbToDollar)).perform(click());
        onView(withId(R.id.imgTo)).check(matches(withTagValue(equalTo(R.drawable.ic_dollar))));
    }

    @Test
    public void shouldShowPoundAsToCurrencyIconWhenRbToPoundSelected() {
        onView(withId(R.id.rbToPound)).perform(click());
        onView(withId(R.id.imgTo)).check(matches(withTagValue(equalTo(R.drawable.ic_pound))));
    }

    // Test Exchange button

    @Test
    public void shouldShowToastWhenExchangePressed() {
        onView(withId(R.id.txtAmount)).perform(replaceText("1"));
        onView(withId(R.id.btnExchange)).perform(click());
        onView(withText("1.00 € = 1.17 $")).inRoot(withDecorView(not(is(activityTestRule
                .getActivity()
                .getWindow()
                .getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowWriteZeroOnAmountWhenNoValidAmount() {
        onView(withId(R.id.txtAmount)).perform(replaceText(""));
        onView(withId(R.id.btnExchange)).perform(click());
        onView(withId(R.id.txtAmount)).check(matches(withText("0.00")));
    }

    // Test amount

    @Test
    public void shouldAmountBeSelectedWhenGainFocus() {
        onView(withId(R.id.txtAmount)).perform(pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.txtAmount)).perform(click());
        onView(withId(R.id.txtAmount)).check(matches(hasAllTextSelected()));
    }

    public static Matcher<View> hasAllTextSelected() {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView)) {
                    return false;
                }
                TextView textView = (TextView) view;
                String selectedText = textView.getText().toString().substring(textView.getSelectionStart(), textView
                        .getSelectionEnd());
                return TextUtils.equals(textView.getText().toString(), selectedText);
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("hasAllTextSelected=true");
            }
        };
    }

}
