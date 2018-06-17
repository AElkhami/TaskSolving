package com.elkhamitech.tasksolving.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elkhamitech.tasksolving.bases.BaseFragment;
import com.elkhamitech.tasksolving.bases.BasePresenter;
import com.elkhamitech.tasksolving.bases.BasePresenterListener;
import com.elkhamitech.tasksolving.presenter.MainContract;
import com.elkhamitech.tasksolving.presenter.OnBackPressedListener;
import com.elkhamitech.tasksolving.presenter.PresenterImpl;
import com.etisalat.sampletask.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener, OnBackPressedListener, BasePresenterListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView mealDesc;
    private TextView meanPrice;
    private ImageView mealImage;
    private MainContract.Presenter presenter;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        presenter = new PresenterImpl(this);

        initUI(view);
        presenter.receiveFoodData(collapsingToolbarLayout, mealDesc, meanPrice, this);

        return view;
    }


    private void initUI(View view) {
        Toolbar toolbar = view.findViewById(R.id.details_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);

        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        mealDesc = view.findViewById(R.id.meal_dec_textView);
        meanPrice = view.findViewById(R.id.meal_price_textView);
        mealImage = view.findViewById(R.id.toolbarImage);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //swipe to dissmiss impmentaion (NOT DONE YET)
    @Override
    protected BasePresenter setupPresenter() {
        return null;
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            // Fully expanded
        } else {
            // Not fully expanded or collapsed
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //on up button pressed
            case android.R.id.home:

                presenter.onBackButtonclicked(this);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem sortItem = menu.findItem(R.id.sort_list_menu);
        MenuItem switchLinearItem = menu.findItem(R.id.swtich_list_menu);
        MenuItem switchGridItem = menu.findItem(R.id.swtich_grid_menu);
        MenuItem refreshItem = menu.findItem(R.id.refresh_list_menu);

        //hide all Activity menu items to not be shown in the fragment
        sortItem.setVisible(false);
        switchLinearItem.setVisible(false);
        switchGridItem.setVisible(false);
        refreshItem.setVisible(false);
    }

    @Override
    public boolean onBackPressed() {

        presenter.onBackButtonclicked(this);

        return true;
    }
}
