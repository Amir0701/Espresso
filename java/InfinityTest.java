import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;

import static org.hamcrest.Matchers.allOf;

import android.app.Activity;
import android.content.ComponentName;
import android.os.IBinder;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.Root;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import java.util.Collection;

import ml.docilealligator.infinityforreddit.R;
import ml.docilealligator.infinityforreddit.activities.MainActivity;
import ml.docilealligator.infinityforreddit.activities.LoginActivity;
import ml.docilealligator.infinityforreddit.activities.EditProfileActivity;

@RunWith(AndroidJUnit4.class)
public class InfinityTest {

    private static ProfileActivityPage profileActivityPage;
    private static MainActivityPage mainActivityPage;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public ActivityScenarioRule<LoginActivity> loginRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp(){
        mainActivityPage = new MainActivityPage();
        profileActivityPage = new ProfileActivityPage();
    }

    @Test
    public void enterAccount() {
        onView(allOf(ViewMatchers.withContentDescription("Открыть панель навигации"), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.profile_image_view_nav_header_main), isDisplayed())).perform(click());
        onView(allOf(withText("juseppee"), isDisplayed())).perform(click());
    }

    @Test
    public void deleteDescription() throws InterruptedException {
        mainActivityPage.tapOnMore();
//        onView(allOf(ViewMatchers.withContentDescription("Открыть панель навигации"), isDisplayed())).perform(click());
        onView(allOf(withText("Профиль"), isDisplayed())).perform(click());
        onView(allOf(ViewMatchers.withContentDescription("Другие параметры"), isDisplayed())).perform(click());
        onView(allOf(withText("Edit Profile"), isDisplayed())).perform(click());
        Thread.sleep(2000);
        onView(allOf(withId(R.id.edit_text_about_you_edit_profile_activity), isDisplayed())).perform(clearText());
        Thread.sleep(2000);
        swipeDown();
        onView(allOf(ViewMatchers.withContentDescription("Сохранить"), isDisplayed())).perform(click());
        onView(allOf(ViewMatchers.withContentDescription("Перейти вверх"), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.toolbar_view_user_detail_activity))).check(matches(isDisplayed()));
    }


    @Test
    public void addDescription() {
        onView(allOf(ViewMatchers.withContentDescription("Открыть панель навигации"), isDisplayed())).perform(click());
        onView(allOf(withText("Профиль"), isDisplayed())).perform(click());
        onView(allOf(ViewMatchers.withContentDescription("Другие параметры"), isDisplayed())).perform(click());
        onView(allOf(withText("Edit Profile"), isDisplayed())).perform(click());
        onView(allOf(withHint("A little description of yourself"), isDisplayed())).perform(typeText("espresso testing"));
        onView(allOf(withId(android.R.id.content), isDisplayed())).perform(pressBack());
        swipeDown();
        onView(allOf(ViewMatchers.withContentDescription("Сохранить"), isDisplayed())).perform(click());
        onView(allOf(ViewMatchers.withContentDescription("Перейти вверх"), isDisplayed())).perform(click(), closeSoftKeyboard());
        onView(allOf(withId(R.id.toolbar_view_user_detail_activity))).check(matches(isDisplayed()));
    }

    @Test
    public void toastChecking(){
        onView(allOf(ViewMatchers.withContentDescription("Открыть панель навигации"), isDisplayed())).perform(click());
        onView(allOf(withText("Входящие"), isDisplayed())).perform(click());
        onView(allOf(ViewMatchers.withContentDescription("Другие параметры"), isDisplayed())).perform(click());
        onView(allOf(withText("Прочитать все сообщения"), isDisplayed())).perform(click());
        onView(withText(R.string.please_wait)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    Activity currentActivity = null;

    public Activity getActivityInstance(){
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities =
                        ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity = (Activity) resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity;
    }

}
class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override    public void describeTo(Description description) {
        description.appendText("is toast");
    }

    @Override    public boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
            IBinder windowToken = root.getDecorView().getWindowToken();
            IBinder appToken = root.getDecorView().getApplicationWindowToken();
            if (windowToken == appToken) {
                //means this window isn't contained by any other windows.
                return true;
            }
        }
        return false;
    }
}


