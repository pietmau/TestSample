package tests.ui;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import com.pietrantuono.pietrantuonoevaluationtask.activities.MainActivity;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class UITest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;
    private MainActivity mActivity;

    public UITest() {
        super(MainActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test1(){
        assertTrue(solo.waitForText("Article 8"));
        solo.clickOnText("Article 8");
        assertTrue(solo.waitForText("laborum. Article 8"));
        solo.goBack();

    }

    public void testRotation1() throws InterruptedException {
        assertTrue(solo.waitForText("Article 8"));
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Thread.sleep(2 * 1000);
        mActivity= (MainActivity) solo.getCurrentActivity();
        assertTrue(solo.searchText("Article 8"));
        solo.clickOnText("Article 8");
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Thread.sleep(2 * 1000);
        mActivity= (MainActivity) solo.getCurrentActivity();
        assertTrue(solo.waitForText("laborum. Article 8"));
        solo.goBack();

    }

}
