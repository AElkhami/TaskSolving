package com.elkhamitech.tasksolving.presenter;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.elkhamitech.tasksolving.bases.BasePresenterListener;
import com.elkhamitech.tasksolving.data.model.Food;
import com.elkhamitech.tasksolving.ui.DetailsFragment;
import com.elkhamitech.tasksolving.ui.adapter.FoodAdapter;

import java.util.List;

public interface MainContract {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     */

    interface Presenter {
        void onDestroy();

        boolean onRefreshData(Context context);

        void onRequestData(Context context);

        void sendFoodData(Food foodData, DetailsFragment detailsFragment);
        void receiveFoodData(CollapsingToolbarLayout collapsingToolbarLayout, TextView mealDesc, TextView meanPrice, Fragment context);

        void replaceFragment(DetailsFragment detailsFragment, FragmentManager manager);

        void switchLayoutView(boolean viewSwitchedFlag, FoodAdapter adapter, RecyclerView recyclerView, Context context);

        void setLastUpdate(TextView lastUpdated, Context context);

        void sortNumeric(List<Food> foodList);

        void sortAlfa(List<Food> foodList);

        void onBackButtonclicked(Fragment context);

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressDialogue
     * while the setDataToRecyclerView and onResponseFailure is fetched from the RestInteractorImpl class
     **/

    interface MainView extends BasePresenterListener {

        @Override
        void showProgress();

        @Override
        void hideProgress();

        void setDataToRecyclerView(List<Food> foodList);

        void onResponseFailure(Throwable throwable);
    }

    /**
     * Interactors are classes built for fetching data from your database, web services, or any other data source.
     **/

    interface RestInteractor {

        interface OnFinishedListener {
            void onFinished(List<Food> FoodList);

            void onFailure(Throwable throwable);
        }

        void getFoodList(OnFinishedListener onFinishedListener, Context context);

    }

    interface PreferenceInteractor {

        void setViewPreference(boolean viewSwitchedFlag);

        void setLastUpdPreference(String lastUpdated);

        Boolean getViewPreference();

        String getLastUpdPreference();

    }

}
