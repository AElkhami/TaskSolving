package com.elkhamitech.tasksolving.data.model;

import org.simpleframework.xml.ElementList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FoodResponse {

    @ElementList(inline = true)
    private List<Food> foodList;

    public List<Food> getFoodList() {

        Collections.sort(foodList, new Comparator<Food>() {
            @Override
            public int compare(Food food, Food t1) {
                return food.getName().compareTo(t1.getName());
            }
        });
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }
}
