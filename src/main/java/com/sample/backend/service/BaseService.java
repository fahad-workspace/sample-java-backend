package com.sample.backend.service;

import com.sample.backend.exception.EntityNotFoundException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<T, ID> {

  protected final JpaRepository<T, ID> repository;

  protected BaseService(JpaRepository<T, ID> repository) {
    this.repository = repository;
  }

  public List<T> findAll() {
    return repository.findAll();
  }

  public T findById(ID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Entity not found with ID: " + id));
  }

  public T save(T entity) {
    return repository.save(entity);
  }

  public void deleteById(ID id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("Entity not found with ID: " + id);
    }
    repository.deleteById(id);
  }
}
