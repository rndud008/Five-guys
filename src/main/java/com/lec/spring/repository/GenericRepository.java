package com.lec.spring.repository;

public interface GenericRepository<T> {
    int save(T entity);
}
