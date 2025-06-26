package com.gtarp.tabarico.entities.accounting;

public enum TypeOfStockMovement {
    customerSale ("Vente Client"),
    stockModification ("Modification du stock");

    private final String name;
    TypeOfStockMovement(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
