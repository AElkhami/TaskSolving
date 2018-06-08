package com.elkhamitech.tasksolving.data.model;

import org.simpleframework.xml.ElementList;

import java.util.List;

public class FoodResponse {

    @ElementList(inline = true)
    public List<Food> foodList;

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }
}
