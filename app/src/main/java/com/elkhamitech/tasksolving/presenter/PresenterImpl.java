package com.elkhamitech.tasksolving.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.widget.TextView;

import com.elkhamitech.tasksolving.bases.BasePresenter;
import com.elkhamitech.tasksolving.bases.BasePresenterListener;
import com.elkhamitech.tasksolving.data.model.Food;
import com.elkhamitech.tasksolving.ui.DetailsFragment;
import com.elkhamitech.tasksolving.ui.MainActivity;
import com.elkhamitech.tasksolving.ui.adapter.FoodAdapter;
import com.etisalat.sampletask.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PresenterImpl extends BasePresenter implements MainContract.Presenter, MainContract.RestInteractor.OnFinishedListener {

    private MainContract.MainView mainView;
    private MainContract.RestInteractor restInteractor;

    public PresenterImpl(BasePresenterListener listener, MainContract.MainView mainView, MainContract.RestInteractor restInteractor) {
        super(listener);
        this.mainView = mainView;
        this.restInteractor = restInteractor;
    }

    public PresenterImpl(BasePresenterListener listener){
        super(listener);

    }


    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public boolean onRefreshData(Context context) {

        if (mainView != null) {
            mainView.showProgress();
        }
        restInteractor.getFoodList(this, context);

        return false;

    }

    @Override
    public void onRequestData(Context context) {
        restInteractor.getFoodList(this, context);
        mainView.showProgress();
    }

    @Override
    public void sendFoodData(Food food, DetailsFragment detailsFragment) {
        Bundle bundle = new Bundle();
        bundle.putString("meal_name", food.getName());
        bundle.putString("meal_desc", food.getDescription());
        bundle.putInt("meal_id", food.getId());
        bundle.putString("meal_price", food.getCost());
        // set MyFragment Arguments
        detailsFragment.setArguments(bundle);
    }

    @Override
    public void receiveFoodData(CollapsingToolbarLayout collapsingToolbarLayout, TextView mealDesc, TextView meanPrice, Fragment context) {
        if (context.getArguments() != null) {
            collapsingToolbarLayout.setTitleEnabled(true);
            collapsingToolbarLayout.setTitle(context.getArguments().getString("meal_name"));
            mealDesc.setText(context.getArguments().getString("meal_desc"));
            meanPrice.setText(String.format("Price %s $", context.getArguments().getString("meal_price")));
        }
    }

    @Override
    public void replaceFragment(DetailsFragment detailsFragment, FragmentManager manager) {

        FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down);
        transaction.replace(R.id.fragment_container, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void switchLayoutView(boolean viewSwitchedFlag, FoodAdapter adapter, RecyclerView recyclerView, Context context) {

        recyclerView.setLayoutManager(viewSwitchedFlag ? new LinearLayoutManager(context)
                : new GridLayoutManager(context, 2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void setLastUpdate(TextView lastUpdated, Context context) {
        String currentDateTimeString;
        Calendar mDate = Calendar.getInstance();

        //compare if the last updated is today.
        if (DateUtils.isToday(mDate.getTimeInMillis())) {

            currentDateTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
            String lastUpdText = "last updated today, " + currentDateTimeString;
            lastUpdated.setText(lastUpdText);

        } else {

            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            lastUpdated.setText(currentDateTimeString);
        }

    }

    @Override
    public void sortNumeric(List<Food> foodList) {
        Collections.sort(foodList, new Comparator<Food>() {
            @Override
            public int compare(Food food, Food t1) {
                return food.getCost().compareTo(t1.getCost());

            }
        });
    }

    @Override
    public void sortAlfa(List<Food> foodList) {
        Collections.sort(foodList, new Comparator<Food>() {
            @Override
            public int compare(Food food, Food t1) {
                return food.getName().compareTo(t1.getName());
            }
        });
    }

    @Override
    public void onBackButtonclicked(Fragment context) {
        if (context.getFragmentManager() != null) {
//                    getFragmentManager().popBackStack();
            context.getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down)
                    .remove(context)
                    .commit();

            //refresh the host activity's toolBar when closing the fragment
            ((MainActivity) context.getActivity()).refreshToolBar();
        }
    }

    @Override
    public void onFinished(List<Food> FoodList) {

        if (mainView != null) {
            mainView.setDataToRecyclerView(FoodList);
            mainView.hideProgress();
        }

    }

    @Override
    public void onFailure(Throwable throwable) {

        if (mainView != null) {
            mainView.onResponseFailure(throwable);
            mainView.hideProgress();
        }
    }
}
