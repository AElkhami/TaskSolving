package com.elkhamitech.tasksolving.presenter;

import com.elkhamitech.tasksolving.bases.BasePresenterListener;
import com.elkhamitech.tasksolving.data.model.Food;

import java.util.List;

public interface MainContract {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     * */

    interface Presenter{
        void onDestroy();
        void onRefresh();
        void onRequestData();

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressDialogue
     * while the setDataToRecyclerView and onResponseFailure is fetched from the InteractorImpl class
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

    interface Interactor{

        interface OnFinishedListener {
            void onFinished(List<Food> FoodList);
            void onFailure(Throwable throwable);
        }

        void getFoodList(OnFinishedListener onFinishedListener);

    }

}
