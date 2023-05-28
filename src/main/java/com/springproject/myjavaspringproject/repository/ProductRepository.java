package com.springproject.myjavaspringproject.repository;

import com.springproject.myjavaspringproject.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , Integer> {

    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String name ) ;
    List<Product> findByNameContainingIgnoreCaseOrUrlContainingOrPriceOrderByIdAsc(String name , String url  , Double price);




}
