package com.elkhamitech.tasksolving.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elkhamitech.tasksolving.bases.BaseControllerListener;
import com.elkhamitech.tasksolving.data.model.Food;
import com.etisalat.sampletask.R;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements BaseControllerListener {

    private Context context;
    private List<Food> foodList;
    public boolean viewSwitchedFlag;
    private RecyclerItemClickListener recyclerItemClickListener;

     FoodAdapter(Context context, List<Food> foodList, boolean viewSwitchedFlag, RecyclerItemClickListener recyclerItemClickListener) {
        this.context = context;
        this.foodList = foodList;
        this.viewSwitchedFlag = viewSwitchedFlag;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int id;
        private TextView nameText;
        private TextView descText;
        private TextView coastText;
        private ImageView foodImageView;

        ViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_textView);
            descText = itemView.findViewById(R.id.desc_textView);
            coastText = itemView.findViewById(R.id.coast_textView);
            foodImageView = itemView.findViewById(R.id.item_img_imageView);

        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, DetailsFragment.class));
            Toast.makeText(context, String.valueOf(id), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(viewSwitchedFlag ? R.layout.food_list_item : R.layout.food_grid_item, parent, false);

        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final FoodAdapter.ViewHolder holder, int position) {

        Food foodModel = foodList.get(holder.getAdapterPosition());

        holder.id = foodModel.getId();

        holder.nameText.setText(foodModel.getName());

        if (viewSwitchedFlag) {
            holder.descText.setText(foodModel.getDescription());
        }

        holder.coastText.setText(String.format("%s $", String.valueOf(foodModel.getCost())));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(android.R.color.darker_gray);

        Glide.with(context)
                .applyDefaultRequestOptions(requestOptions)
                .load(R.mipmap.ic_image_icon)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.foodImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerItemClickListener.onItemClick(foodList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


}
