package com.example.ecommerce.service.products;


import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }


    public Product updateProduct(Long id, Product product) {
       return productRepository.findById(id)
                .map(p -> {
                    p.setName(product.getName());
                    p.setPrice(product.getPrice());
                    p.setDescription(product.getDescription());
                    p.setCategory(product.getCategory());
                    p.setStock(product.getStock());
                    p.setImage(product.getImage());
                    return productRepository.save(p);
                })
               .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
