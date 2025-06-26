package com.gtarp.tabarico.entities.accounting;

public enum TypeOfSale {
    cleanMoney ("Argent propre"),
    dirtyMoney ("Argent sale");

    private final String name;
    TypeOfSale(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
