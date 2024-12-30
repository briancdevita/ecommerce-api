package com.example.ecommerce.service.cart;

import com.example.ecommerce.DTO.CartDTO;
import com.example.ecommerce.DTO.CartItemDTO;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }






    /**
     * Obtiene el carrito de un usuario y lo convierte a DTO.
     */
    public CartDTO getCartByUser(User user) {
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });

        return convertCartToDTO(cart);
    }

    /**
     * Agrega un producto al carrito del usuario.
     */
    public CartDTO addProductToCart(User user, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Verifica si el producto ya está en el carrito
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Actualiza la cantidad del producto
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // Crea un nuevo CartItem
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cartItemRepository.save(cartItem);
        }

        // Devuelve el carrito actualizado como DTO
        return convertCartToDTO(cart);
    }

    /**
     * Elimina un producto del carrito del usuario.
     */
    public void removeProductFromCart(User user, Long productId) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }

    /**
     * Convierte una entidad `Cart` a un `CartDTO`.
     */
    private CartDTO convertCartToDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUsername(cart.getUser().getUsername());
        cartDTO.setItems(cart.getCartItems().stream().map(this::convertCartItemToDTO).collect(Collectors.toList()));
        return cartDTO;
    }

    /**
     * Convierte una entidad `CartItem` a un `CartItemDTO`.
     */
    private CartItemDTO convertCartItemToDTO(CartItem item) {
        CartItemDTO itemDTO = new CartItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setProductId(item.getProduct().getId());
        itemDTO.setProductName(item.getProduct().getName());
        itemDTO.setQuantity(item.getQuantity());
        return itemDTO;
    }


    @Transactional
    public void clearCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public void updateQuantity(User user, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + user.getUsername()));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart: " + productId));

        if (quantity <= 0) {
            // Eliminar el producto del carrito si la cantidad es 0
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            // Actualizar la cantidad
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }


    public Double calculateTotal(CartDTO cartDTO) {
        return cartDTO.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()))
                        .getPrice())
                .sum();
    }







}



