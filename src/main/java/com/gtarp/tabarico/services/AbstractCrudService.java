package com.gtarp.tabarico.services;

import com.gtarp.tabarico.entities.UpdatableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class AbstractCrudService<T extends UpdatableEntity<T, Dto>, R extends JpaRepository<T, Integer>, Dto> implements CrudService<T, Dto> {
    protected R repository;

    public AbstractCrudService(R repository) { this.repository = repository; }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public abstract T insert(Dto dto);

    @Override
    public abstract T getById(Integer id);

    @Override
    public T update(Integer id, Dto dto) {
        T updatedEntity = getById(id).update(dto);
        return repository.save(updatedEntity);
    }

    @Override
    public void delete(Integer id) {
        T entity = getById(id);
        repository.delete(entity);
    }
}
