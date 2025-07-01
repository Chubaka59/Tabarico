package com.gtarp.tabarico.entities.accounting;

public enum OperationStock {
    add ("Ajout"),
    remove ("Retrait");

    private final String name;
    OperationStock(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
