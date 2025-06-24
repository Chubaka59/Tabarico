package com.gtarp.tabarico.services;

import java.util.List;

public interface CrudService<T, Dto> {
    /**
     * get a list of all object
     * @return a list of all object of this entity
     */
    List<T> getAll();

    /**
     * save a new object in the repository
     * @param dto the new object to save
     * @return the object that has been saved
     */
    T insert(Dto dto);

    /**
     * get a specific object of this entity from its id
     * @param id the id of the object
     * @return the object
     */
    T getById(Integer id);

    /**
     * update the information of an object
     * @param id the id of the object to update
     * @param dto the updated information
     */
    T update(Integer id, Dto dto);

    /**
     * delete an object in the repository
     * @param id the id of the object to delete
     */
    void delete(Integer id);
}
