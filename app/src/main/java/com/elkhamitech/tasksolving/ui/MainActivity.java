package com.elkhamitech.tasksolving.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements MainContract.MainView,BasePresenterListener {

    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
    private MainContract.Presenter presenter;
    private TextView lastUpdated;
    private SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initUI();

        presenter = new PresenterImpl(MainActivity.this,MainActivity.this, new InteractorImpl());
        presenter.onRequestData();

        setLastUpdated();

    }

    /*
     * Initialise UI Components
     */
    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        constraintLayout = findViewById(R.id.constraint_layout);
        lastUpdated = findViewById(R.id.time_stamp_textView);
        recyclerView = findViewById(R.id.food_recyclerView);
        swipeLayout = findViewById(R.id.swipeContainer);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeLayout.setRefreshing(presenter.onRefreshData());
                    }
                }, 1000);


                setLastUpdated();

            }
        });

        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));

    }

    private void setLastUpdated() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        lastUpdated.setText(currentDateTimeString);
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

        FoodAdapter adapter = new FoodAdapter(this,foodList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        showSnackbar(throwable.getMessage(),constraintLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
