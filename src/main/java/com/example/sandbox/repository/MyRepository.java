package com.example.sandbox.repository;

import com.example.sandbox.entity.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyRepository extends JpaRepository<MyEntity, Long> {


}
