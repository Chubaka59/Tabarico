package com.gtarp.tabarico.dto.accouting;

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
