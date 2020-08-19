package com.softhub.vendor;

public class FetchOnlineApprove {

    private String id;
    private String name;
    private String order_id;
    private String meal_Name;
    private String meal_Time;
    private String meal_Type;
    private String count;
    private String price;

    public FetchOnlineApprove(String id, String name, String order_id, String meal_Name, String meal_Time, String meal_Type, String count, String price) {
        this.id = id;
        this.name = name;
        this.order_id = order_id;
        this.meal_Name = meal_Name;
        this.meal_Time = meal_Time;
        this.meal_Type = meal_Type;
        this.count = count;
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMeal_Name() {
        return meal_Name;
    }

    public void setMeal_Name(String meal_Name) {
        this.meal_Name = meal_Name;
    }

    public String getMeal_Time() {
        return meal_Time;
    }

    public void setMeal_Time(String meal_Time) {
        this.meal_Time = meal_Time;
    }

    public String getMeal_Type() {
        return meal_Type;
    }

    public void setMeal_Type(String meal_Type) {
        this.meal_Type = meal_Type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
