package com.hemendra.springular.entity.repos;

import com.hemendra.springular.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BaseEntityRepo extends CrudRepository<BaseEntity, Long> {
}
