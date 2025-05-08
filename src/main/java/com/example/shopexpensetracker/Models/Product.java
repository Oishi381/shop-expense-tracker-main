package com.example.shopexpensetracker.Models;



public class Product {
    private final Integer productID;
    private final String productName;
    private final Double productPrice;
    private final Integer productStock;

    public Product() {
        this.productID = 0;
        this.productName = "";
        this.productPrice = 0.00;
        this.productStock = 0;
    }
    public Product(Integer productID, String productName, Double productPrice, Integer productStock) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
    }

    public Integer getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

}
