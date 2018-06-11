package com.elkhamitech.tasksolving.data.model;

import com.elkhamitech.tasksolving.bases.BaseController;
import com.elkhamitech.tasksolving.bases.BaseControllerListener;

import org.simpleframework.xml.ElementList;

import java.util.List;

public class FoodResponse extends BaseController{

    @ElementList(inline = true)
    private List<Food> foodList;

    public FoodResponse(){

    }

    public FoodResponse(BaseControllerListener listener) {
        super(listener);
    }


    public List<Food> getFoodList() {

        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }
}
