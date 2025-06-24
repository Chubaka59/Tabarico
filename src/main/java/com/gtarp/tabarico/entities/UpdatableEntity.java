package com.gtarp.tabarico.entities;

public interface UpdatableEntity<T, Dto> {

    T update(Dto dto);

}