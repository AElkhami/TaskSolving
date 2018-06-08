package com.elkhamitech.tasksolving.presenter;

import com.elkhamitech.tasksolving.bases.BasePresenter;
import com.elkhamitech.tasksolving.bases.BasePresenterListener;
import com.elkhamitech.tasksolving.data.model.Food;


import java.util.List;

public class PresenterImpl extends BasePresenter implements MainContract.Presenter, MainContract.Interactor.OnFinishedListener {

    private MainContract.MainView mainView;
    private MainContract.Interactor interactor;

    public PresenterImpl(BasePresenterListener listener, MainContract.MainView mainView, MainContract.Interactor interactor) {
        super(listener);
        this.mainView = mainView;
        this.interactor = interactor;
    }


    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public boolean onRefreshData() {

        if(mainView != null){
            mainView.showProgress();
        }
        interactor.getFoodList(this);

        return false;

    }

    @Override
    public void onRequestData() {
        interactor.getFoodList(this);
        mainView.showProgress();
    }

    @Override
    public void onFinished(List<Food> FoodList) {

        if(mainView != null){
            mainView.setDataToRecyclerView(FoodList);
            mainView.hideProgress();
        }

    }

    @Override
    public void onFailure(Throwable throwable) {

        if(mainView != null){
            mainView.onResponseFailure(throwable);
            mainView.hideProgress();
        }
    }
}
