package com.elkhamitech.tasksolving.ui;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;

import com.elkhamitech.tasksolving.bases.BaseActivity;
import com.elkhamitech.tasksolving.bases.BasePresenter;
import com.elkhamitech.tasksolving.bases.BasePresenterListener;
import com.elkhamitech.tasksolving.data.model.Food;
import com.elkhamitech.tasksolving.presenter.InteractorImpl;
import com.elkhamitech.tasksolving.presenter.MainContract;
import com.elkhamitech.tasksolving.presenter.PresenterImpl;
import com.etisalat.sampletask.R;

import java.text.DateFormat;
import java.util.Calendar;
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
    private boolean viewSwitchedFlag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        presenter = new PresenterImpl(MainActivity.this, MainActivity.this, new InteractorImpl());
        presenter.onRequestData(this);

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

        constraintLayout = findViewById(R.id.constraint_layout);
        lastUpdated = findViewById(R.id.time_stamp_textView);
        recyclerView = findViewById(R.id.food_recyclerView);
        swipeLayout = findViewById(R.id.swipeContainer);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void setLastUpdated() {
        String currentDateTimeString;
        Calendar mDate = Calendar.getInstance();

        //compare if the last updated is today.
        if (DateUtils.isToday(mDate.getTimeInMillis())) {

            currentDateTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
            lastUpdated.setText("last updated today, " + currentDateTimeString);

        } else {

            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            lastUpdated.setText(currentDateTimeString);
        }


    }

    private void refreshList(){
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refresh data while return if the refresh is done.
                swipeLayout.setRefreshing(presenter.onRefreshData(MainActivity.this));

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

        adapter = new FoodAdapter(this, foodList);
        recyclerView.setAdapter(adapter);

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
            case R.id.refresh_list:

                presenter.onRefreshData(MainActivity.this);
                setLastUpdated();

                return true;
            case R.id.swtich:

                switchView();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void switchView(){


        viewSwitchedFlag = !viewSwitchedFlag;
        adapter.viewSwitchedFlag = !adapter.viewSwitchedFlag;

        menu.getItem(0).setIcon(ContextCompat.getDrawable(this,viewSwitchedFlag ? R.drawable.ic_linear_list : R.drawable.ic_grid_list));

        recyclerView.setLayoutManager(viewSwitchedFlag ? new LinearLayoutManager(this) : new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
