package com.example.ecommerce.exception.error;




public class InsufficientStockException extends RuntimeException {
    private final Long productId;
    private final Integer availableStock;

    public InsufficientStockException(Long productId, Integer availableStock) {
        super("Insufficient stock for product with id: " + productId + ". Available stock: " + availableStock);
        this.productId = productId;
        this.availableStock = availableStock;
    }


    public Long getProductId() {
        return productId;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }
}
