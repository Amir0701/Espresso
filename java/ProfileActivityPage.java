import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.matcher.ViewMatchers;

public class ProfileActivityPage {
    void tapOnMoreProfile(){
        onView(allOf(ViewMatchers.withContentDescription("Другие параметры"), isDisplayed())).perform(click());
    }

    void editProfile(){
        onView(allOf(withText("Edit Profile"), isDisplayed())).perform(click());
    }
}
