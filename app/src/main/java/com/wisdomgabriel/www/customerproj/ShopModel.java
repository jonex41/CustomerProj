package com.wisdomgabriel.www.customerproj;

import java.io.Serializable;

public class ShopModel implements Serializable {

    private String imageUrl, foodName, foodPrice, foodDescription, price_per_one, platesNumber, sitnumber;


    public ShopModel() {
    }

    public String getSitnumber() {
        return sitnumber;
    }

    public void setSitnumber(String sitnumber) {
        this.sitnumber = sitnumber;
    }

    public String getPlatesNumber() {
        return platesNumber;
    }

    public void setPlatesNumber(String platesNumber) {
        this.platesNumber = platesNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice_per_one() {
        return price_per_one;
    }

    public void setPrice_per_one(String price_per_one) {
        this.price_per_one = price_per_one;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }
}
