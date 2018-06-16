package com.elkhamitech.tasksolving.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.etisalat.sampletask.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener, OnBackPressedListener {


    private boolean isPressed = true;
    private CardView cvSwipeDismiss;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView mealDesc;
    private TextView meanPrice;
    private ImageView mealImage;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        initUI(view);
        getDataFromActivity();

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

    private void getDataFromActivity() {
        if (getArguments() != null) {
            collapsingToolbarLayout.setTitleEnabled(true);
            collapsingToolbarLayout.setTitle(getArguments().getString("meal_name"));
            mealDesc.setText(getArguments().getString("meal_desc"));
            meanPrice.setText(String.format("Price %s $",getArguments().getString("meal_price")));
            int mealId = getArguments().getInt("meal_id_desc");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Activity activity = getActivity();
//
//        Toolbar toolbar = activity.findViewById(R.id.details_toolbar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.setActionBar(toolbar);
////            activity.getActionBar().setDisplayShowHomeEnabled(true);
//        }


    }

    //swipe to dissmiss impmentaion (NOT DONE YET)
    @Override
    protected BasePresenter setupPresenter() {
        return null;
    }

    private void swipeDismiss() {
        //<V> - The View type that this Behavior operates on
        SwipeDismissBehavior<CardView> swipeDismissBehavior = new SwipeDismissBehavior();

        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END );

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) cvSwipeDismiss.getLayoutParams();
        layoutParams.setMargins(10,10,10,10);
        layoutParams.setBehavior(swipeDismissBehavior);

    }

    private void setUpViews(View views) {
        cvSwipeDismiss = views.findViewById(R.id.main_content);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            // Fully expanded
        }
        else {
            // Not fully expanded or collapsed
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            //on up button pressed
            case android.R.id.home:
                if (getFragmentManager() != null) {
//                    getFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down)
                            .remove(this)
                            .commit();
                    //refresh the host activity's toolBar when closing the fragment
                    ((MainActivity)getActivity()).refreshToolBar();
                }
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
        if (isPressed) {
            if (getFragmentManager() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down)
                        .remove(this)
                        .commit();

                //refresh the host activity's toolBar when closing the fragment
                ((MainActivity)getActivity()).refreshToolBar();
            }
            return true;
        } else {
            return false;
        }
    }
}
