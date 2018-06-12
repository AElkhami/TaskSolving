package com.elkhamitech.tasksolving.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
public class DetailsFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener{


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        TextView mealDesc = view.findViewById(R.id.meal_dec_textView);
        ImageView mealImage = view.findViewById(R.id.toolbarImage);


        if (getArguments() != null) {
            collapsingToolbarLayout.setTitleEnabled(true);
            collapsingToolbarLayout.setTitle(getArguments().getString("meal_name"));
            mealDesc.setText(getArguments().getString("meal_desc"));
            int mealId = getArguments().getInt("mealmeal_id_desc");
        }


        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();

    }

    @Override
    protected BasePresenter setupPresenter() {
        return null;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0)
        {
            // Fully expanded
        }
        else
        {
            // Not fully expanded or collapsed
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
