package com.example.KTCK.service;

import com.example.KTCK.model.Products;
import com.example.KTCK.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }
    public Optional <Products> getProductById(Long id) {
        return productRepository.findById(id);
    }
    public Products saveProduct(Products product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public Page<Products> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }


}
