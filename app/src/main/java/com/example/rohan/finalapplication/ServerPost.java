package com.example.rohan.finalapplication;

/**
 * Created by ShoshanaGeller on 11/18/17.
 */

public class ServerPost {

    String name;
    String image;
    String description;
    String price;
    String email;

    public ServerPost() {

    }

    public ServerPost(String name, String image, String description, String price, String email) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }
    public String getDescription() {
        return description;
    }
    public String getPrice() {
        return price;
    }
    public String getEmail() {
        return email;
    }

}
