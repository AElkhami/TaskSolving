package com.elkhamitech.tasksolving.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.elkhamitech.tasksolving.bases.BaseActivity;
import com.elkhamitech.tasksolving.bases.BasePresenter;
import com.elkhamitech.tasksolving.bases.BasePresenterListener;
import com.elkhamitech.tasksolving.data.model.Food;
import com.elkhamitech.tasksolving.presenter.InteractorImpl;
import com.elkhamitech.tasksolving.presenter.MainContract;
import com.elkhamitech.tasksolving.presenter.PresenterImpl;
import com.elkhamitech.tasksolving.presenter.Utils;
import com.etisalat.sampletask.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements MainContract.MainView, BasePresenterListener {

    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
    private MainContract.Presenter presenter;
    private TextView lastUpdated;
    private SwipeRefreshLayout swipeLayout;
    private FoodAdapter adapter;
    private Menu menu;
    private List<Food> foodList;
    private boolean viewSwitchedFlag = true;
    private FloatingActionButton takePhotoFab;
    private static final String SHARED_PREFERENCES = "MyPrefsFile";
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private  DetailsFragment detailsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        presenter = new PresenterImpl(MainActivity.this, MainActivity.this, new InteractorImpl());
        presenter.onRequestData(this);

        setRecyclerViewScrollListener();
        setLastUpdated();
        refreshList();

    }

    /*
     * Initialise UI Components
     */
    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        editor = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        constraintLayout = findViewById(R.id.constraint_layout);
        lastUpdated = findViewById(R.id.time_stamp_textView);
        recyclerView = findViewById(R.id.food_recyclerView);
        swipeLayout = findViewById(R.id.swipeContainer);
        takePhotoFab = findViewById(R.id.takePhoto_FAB);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void setLastUpdated() {
        String currentDateTimeString;
        Calendar mDate = Calendar.getInstance();

        //check for the network
        if (Utils.isNetworkAvailable(MainActivity.this)) {
            //compare if the last updated is today.
            if (DateUtils.isToday(mDate.getTimeInMillis())) {

                currentDateTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
                lastUpdated.setText("last updated today, " + currentDateTimeString);

            } else {

                currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                lastUpdated.setText(currentDateTimeString);
            }

            editor.putString("lastUpd", lastUpdated.getText().toString());
            editor.apply();
        } else {

            String restoredText = prefs.getString("lastUpd", null);
            if (restoredText != null) {
                restoredText = prefs.getString("lastUpd", "");
                lastUpdated.setText(restoredText);
            }

        }


    }

    private void refreshList() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refresh data while return if the refresh is done.
                swipeLayout.setRefreshing(presenter.onRefreshData(MainActivity.this));
                setLastUpdated();

            }
        });

        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
    }


    @Override
    protected BasePresenter setupPresenter() {
        return (BasePresenter) presenter;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void showProgress() {
        super.showProgress();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    @Override
    public void setDataToRecyclerView(List<Food> foodList) {

        adapter = new FoodAdapter(this, foodList, viewSwitchedFlag, recyclerItemClickListener);
        recyclerView.setAdapter(adapter);
        this.foodList = foodList;

    }

    /**
     * RecyclerItem click event listener
     */
    private RecyclerItemClickListener recyclerItemClickListener = new RecyclerItemClickListener() {
        @Override
        public void onItemClick(Food food) {

            detailsFragment = new DetailsFragment();

            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();

            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down);
            transaction.replace(R.id.fragment_container, detailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            sendDataToFragment(food);

        }
    };

    private void sendDataToFragment(Food food) {
        Bundle bundle = new Bundle();
        bundle.putString("meal_name", food.getName());
        bundle.putString("meal_desc", food.getDescription());
        bundle.putInt("meal_id",food.getId());
        bundle.putString("meal_price", food.getCost());
        // set MyFragment Arguments
        detailsFragment.setArguments(bundle);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        showSnackbar(throwable.getMessage(), constraintLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh_list_menu:

                presenter.onRefreshData(MainActivity.this);

                setLastUpdated();

                return true;
            case R.id.swtich_list_menu:

                switchView();

                return true;

            case R.id.sort_alf_list_menu:

                sortAlfa(foodList);
                adapter.notifyDataSetChanged();

                return true;

            case R.id.sort_num_list_menu:

                sortNumeric(foodList);
                adapter.notifyDataSetChanged();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sortNumeric(List<Food> foodList) {
        Collections.sort(foodList, new Comparator<Food>() {
            @Override
            public int compare(Food food, Food t1) {
                return food.getCost().compareTo(t1.getCost());

            }
        });
    }


    private void sortAlfa(List<Food> foodList) {
        Collections.sort(foodList, new Comparator<Food>() {
            @Override
            public int compare(Food food, Food t1) {
                return food.getName().compareTo(t1.getName());
            }
        });
    }


    private void switchView() {

        viewSwitchedFlag = !viewSwitchedFlag;
        adapter.viewSwitchedFlag = !adapter.viewSwitchedFlag;

        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, viewSwitchedFlag ? R.drawable.ic_grid_list : R.drawable.ic_linear_list));

        recyclerView.setLayoutManager(viewSwitchedFlag ? new LinearLayoutManager(this) : new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void goToCapture(View view) {
        Intent goTo = new Intent(MainActivity.this, ImageCaptureActivity.class);
        startActivity(goTo);
    }

    private void setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && takePhotoFab.getVisibility() == View.VISIBLE) {
                    takePhotoFab.hide();
                } else if (dy < 0 && takePhotoFab.getVisibility() != View.VISIBLE) {
                    takePhotoFab.show();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof OnBackPressedListener) || !((OnBackPressedListener) fragment).onBackPressed()) {
            //fragment back pressed
            super.onBackPressed();

        }else {
            //activity back pressed
            super.onBackPressed();
        }

    }
}
