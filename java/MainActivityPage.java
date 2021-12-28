import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.matcher.ViewMatchers;

public class MainActivityPage {
    void tapOnMore(){
        onView(allOf(ViewMatchers.withContentDescription("Открыть панель навигации"), isDisplayed())).perform(click());
    }

    void tapOnProfile(){
        onView(allOf(withText("Профиль"), isDisplayed())).perform(click());
    }


}
