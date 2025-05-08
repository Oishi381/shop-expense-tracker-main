package com.example.shopexpensetracker.Models;

import com.example.shopexpensetracker.Models.Product;

public class ProductCoupon extends Product {
    private final String couponCode;
    private final Double discountPercentage;

    public ProductCoupon(String couponCode, Double discountPercentage, Product p) {
        super(p.getProductID(), p.getProductName(), p.getProductPrice(), p.getProductStock());
        this.couponCode = couponCode;
        this.discountPercentage = discountPercentage;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }
}
