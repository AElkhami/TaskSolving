package com.elkhamitech.tasksolving.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elkhamitech.tasksolving.data.model.Food;
import com.etisalat.sampletask.R;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context context;
    private List<Food> foodList = new ArrayList<>();


    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView foodImage;
        private TextView nameText;
        private TextView descText;
        private TextView coastText;

        ViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_textView);
            descText = itemView.findViewById(R.id.desc_textView);
            coastText = itemView.findViewById(R.id.coast_textView);

        }
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {

        Food foodModel = foodList.get(position);

        int id = foodModel.getId();

        holder.nameText.setText(foodModel.getName());
        holder.descText.setText(foodModel.getDescription());
        holder.coastText.setText(String.format("%s $", String.valueOf(foodModel.getCost())));

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


}
