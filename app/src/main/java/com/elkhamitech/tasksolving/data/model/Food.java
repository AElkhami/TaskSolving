package com.elkhamitech.tasksolving.data.model;

import com.elkhamitech.tasksolving.bases.BaseController;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item")
public class Food extends BaseController {

    @Element(name = "id")
    private int id;

    @Element(name = "name")
    private String name;

    @Element(name = "cost")
    private String  cost;

    @Element(name = "description")
    private String description;

    public Food() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
