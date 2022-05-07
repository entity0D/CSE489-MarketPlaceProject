package com.example.marketplace;


public class model {
    String Product_Name, Products_Picture_URL, Product_ID;
    int Product_Price;

    public model() {
    }

    public model(int price, String name, String Profile_Picture_URL, String id) {
        this.Product_Price = price;
        this.Product_Name = name;
        this.Product_ID = id;
        this.Products_Picture_URL = Profile_Picture_URL;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.Product_ID = product_ID;
    }

    public String getProduct_Price() {
        String str = Integer.toString(Product_Price);
        ;
        return str;
    }

    public void setProduct_Price(int product_Price) {
        this.Product_Price = product_Price;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        this.Product_Name = product_Name;
    }

    public String getProducts_Picture_URL() {
        return Products_Picture_URL;
    }

    public void setProducts_Picture_URL(String products_Picture_URL) {
        this.Products_Picture_URL = products_Picture_URL;
    }
}