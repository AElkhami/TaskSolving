package com.elkhamitech.tasksolving.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.widget.Toolbar;

import com.etisalat.sampletask.BuildConfig;
import com.etisalat.sampletask.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowProgressDialog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityTest {



    private Activity activity;
    private ProgressDialog dialog;
    private ShadowProgressDialog shadow;

    @Before
    public void setUp() throws Exception {

        activity = Robolectric.setupActivity(MainActivity.class);
        dialog = new ProgressDialog(RuntimeEnvironment.application);
        shadow = Shadows.shadowOf(dialog);
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void showProgress() {
//        assertThat(dialog.getProgress()).isEqualTo(0);
    }

    @Test
    public void setDataToRecyclerView() {
        assertNotNull(activity.findViewById(R.id.food_recyclerView));
    }

    @Test
    public void onOptionsItemSelected() {

        //get MainActivity.toolbar instance
        Toolbar toolbar = activity.findViewById(R.id.toolbar);

        //get ShadowActivity instance
        ShadowActivity shadowActivity = shadowOf(activity);

        //call onCreateOptionsMenu using toolbar.menu
        shadowActivity.onCreateOptionsMenu(toolbar.getMenu());

        //assert that OptionsMenu has visible items
        assertTrue(shadowActivity.getOptionsMenu().hasVisibleItems());

        //assert that `Settings` MenuItem is visible
        assertEquals(shadowActivity.getOptionsMenu().findItem(R.id.swtich_list_menu).isVisible(), true);
        assertEquals(shadowActivity.getOptionsMenu().findItem(R.id.refresh_list_menu).isVisible(), true);


        //assert that `Sort` MenuItem is visible
        assertEquals(shadowActivity.getOptionsMenu().getItem(0).getTitle(),
                activity.getString(R.string.switch_menu));
        assertEquals(shadowActivity.getOptionsMenu().getItem(1).getTitle(),
                activity.getString(R.string.refresh_menu));

    }
}