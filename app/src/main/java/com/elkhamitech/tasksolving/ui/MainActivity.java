package com.elkhamitech.tasksolving.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.elkhamitech.tasksolving.bases.BaseActivity;
import com.elkhamitech.tasksolving.bases.BasePresenter;
import com.elkhamitech.tasksolving.bases.BasePresenterListener;
import com.elkhamitech.tasksolving.data.SharedPreferencesHandler;
import com.elkhamitech.tasksolving.data.model.Food;
import com.elkhamitech.tasksolving.presenter.MainContract;
import com.elkhamitech.tasksolving.presenter.OnBackPressedListener;
import com.elkhamitech.tasksolving.presenter.PresenterImpl;
import com.elkhamitech.tasksolving.presenter.RecyclerItemClickListener;
import com.elkhamitech.tasksolving.presenter.RestInteractorImpl;
import com.elkhamitech.tasksolving.presenter.Utils;
import com.elkhamitech.tasksolving.ui.adapter.FoodAdapter;
import com.etisalat.sampletask.R;

import java.util.List;

public class MainActivity extends BaseActivity implements MainContract.MainView, BasePresenterListener {

    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
    private MainContract.Presenter presenter;
    private TextView lastUpdated;
    private SwipeRefreshLayout swipeLayout;
    private FoodAdapter adapter;
    private List<Food> foodList;
    private boolean viewSwitchedFlag = true;
    private FloatingActionButton takePhotoFab;
    private SharedPreferencesHandler preferencesHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initUI();

        presenter = new PresenterImpl(MainActivity.this, MainActivity.this, new RestInteractorImpl());
        presenter.onRequestData(this);

        setRecyclerViewScrollListener();
        setLastUpdated();
        refreshList();

    }

    // Initialise UI Components
    private void initUI() {

        preferencesHandler = new SharedPreferencesHandler(this);

        constraintLayout = findViewById(R.id.constraint_layout);
        lastUpdated = findViewById(R.id.time_stamp_textView);
        recyclerView = findViewById(R.id.food_recyclerView);
        swipeLayout = findViewById(R.id.swipeContainer);
        takePhotoFab = findViewById(R.id.takePhoto_FAB);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        viewSwitchedFlag = preferencesHandler.getViewPreference();
    }

    private void initToolBar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void setLastUpdated() {

        //check for the network
        if (Utils.isNetworkAvailable(MainActivity.this)) {

            presenter.setLastUpdate(lastUpdated, MainActivity.this);

            String lastUpdText = lastUpdated.getText().toString();

            preferencesHandler.setLastUpdPreference(lastUpdText);

        } else {
            String restoredText = preferencesHandler.getLastUpdPreference();
            if (restoredText != null) {
                restoredText = preferencesHandler.getLastUpdPreference();
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

        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright));
    }

    private void switchView() {

        viewSwitchedFlag = !viewSwitchedFlag;
        adapter.viewSwitchedFlag = !adapter.viewSwitchedFlag;

        //save user view choice
        preferencesHandler.setViewPreference(viewSwitchedFlag);

        presenter.switchLayoutView(viewSwitchedFlag, adapter, recyclerView, this);

        adapter.notifyDataSetChanged();
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

                //to hide the action button when scrolling down
                if (dy > 0 && takePhotoFab.getVisibility() == View.VISIBLE) {
                    takePhotoFab.hide();
                } else if (dy < 0 && takePhotoFab.getVisibility() != View.VISIBLE) {
                    takePhotoFab.show();
                }
            }
        });
    }

    public void refreshToolBar() {
        //refresh the toolbar after closig the fragment
        initToolBar();
    }

    // RecyclerItem click event listener
    private RecyclerItemClickListener recyclerItemClickListener = new RecyclerItemClickListener() {
        @Override
        public void onItemClick(Food food) {

            DetailsFragment detailsFragment = new DetailsFragment();
            FragmentManager manager = getSupportFragmentManager();

            presenter.replaceFragment(detailsFragment, manager);
            presenter.sendFoodData(food, detailsFragment);
        }
    };

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


    @Override
    public void onResponseFailure(Throwable throwable) {
        showSnackbar(throwable.getMessage(), constraintLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

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
                invalidateOptionsMenu();

                return true;

            case R.id.swtich_grid_menu:

                switchView();
                invalidateOptionsMenu();

                return true;

            case R.id.sort_alf_list_menu:

                presenter.sortAlfa(foodList);
                adapter.notifyDataSetChanged();

                return true;

            case R.id.sort_num_list_menu:

                presenter.sortNumeric(foodList);
                adapter.notifyDataSetChanged();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem linearItem = menu.findItem(R.id.swtich_list_menu);
        MenuItem gridItem = menu.findItem(R.id.swtich_grid_menu);


        //check for the swtich menu button
        //check whether the user is selected the linear view or the gird view
        if (viewSwitchedFlag) {
            gridItem.setVisible(true);
            linearItem.setVisible(false);
        } else {

            gridItem.setVisible(false);
            linearItem.setVisible(true);
        }

        presenter.switchLayoutView(viewSwitchedFlag, adapter, recyclerView, this);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof OnBackPressedListener) || !((OnBackPressedListener) fragment).onBackPressed()) {
            //fragment back pressed
            super.onBackPressed();
        } else {
            //activity back pressed
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}