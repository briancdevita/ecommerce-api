package com.example.ecommerce.service.products;


import com.example.ecommerce.DTO.ProductSalesDTO;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<ProductSalesDTO> getTopSellingProducts(int limit) {
        List<Object[]> results = productRepository.findTopSellingProducts(limit);

        // Transformar los resultados en un DTO
        return results.stream()
                .map(row -> new ProductSalesDTO(
                        ((Number) row[0]).longValue(), // id
                        (String) row[1],                  // name
                        ((Number) row[2]).intValue(), // total_sold
                        ((Number) row[3]).doubleValue() // total_revenue
                ))
                .collect(Collectors.toList());
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
