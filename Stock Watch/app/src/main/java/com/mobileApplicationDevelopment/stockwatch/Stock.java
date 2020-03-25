package com.mobileApplicationDevelopment.stockwatch;

import java.io.Serializable;

public class Stock implements Serializable {
    private String companySymbol;
    private String companyName;
    private double price;
    private double priceChange;
    private double changePercentage;

    public String getCompanySymbol() {
        return companySymbol;
    }

    public void setCompanySymbol(String companySymbol) {
        this.companySymbol = companySymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(double changePercentage) {
        this.changePercentage = changePercentage;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj == null || obj.getClass() != getClass()) {
            result = false;
        } else {
            Stock stock = (Stock) obj;
            if (this.companySymbol.equals(stock.companySymbol)) {
                result = true;
            }
        }
        return result;
    }

}
