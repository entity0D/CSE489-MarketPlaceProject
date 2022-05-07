package com.example.marketplace;

public class Ads {
    private String Product_ID;
    private int Product_Price;
    private String Product_Name;
    private String Products_Picture_URL;


    public Ads() {
    }

    public Ads(String product_id, int Product_Price, String Product_Name, String Products_Picture_URL) {
        this.Product_ID = product_id;
        this.Product_Price = Product_Price;
        this.Product_Name = Product_Name;
        this.Products_Picture_URL = Products_Picture_URL;
    }

    public String getProducts_Picture_URL() {
        return Products_Picture_URL;
    }

    public void setProducts_Picture_URL(String products_Picture_URL) {
        Products_Picture_URL = products_Picture_URL;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.Product_ID = product_ID;
    }

    public int getProduct_Price() {
        return Product_Price;
    }

    public void setProduct_Price(int product_Price) {
        this.Product_Price = product_Price;
    }

}
