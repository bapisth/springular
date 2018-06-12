package com.hemendra.springular.repository;

import com.hemendra.springular.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Override
    Iterable<User> findAll();

    @Override
    User findOne(Integer integer);
}
