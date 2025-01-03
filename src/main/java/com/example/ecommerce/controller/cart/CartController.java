package com.example.ecommerce.controller.cart;


import com.example.ecommerce.DTO.CartDTO;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.cart.CartService;
import com.example.ecommerce.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
public class CartController {




    private final CartService cartService;
    private final UserService userService;




    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("User not authenticated");
        }
        User user = userService.findByUsername(userDetails.getUsername());
        CartDTO cartDTO = cartService.getCartByUser(user);
        return ResponseEntity.ok(cartDTO);
    }


    @PostMapping("/add")
    public ResponseEntity<CartDTO> addProductToCart(
            @AuthenticationPrincipal User username,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.addProductToCart(username, productId, quantity));
    }


    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.ok("Cart cleared");
    }


    @PutMapping("/update-quiantity")
    public  ResponseEntity<String> updateQuantity(@AuthenticationPrincipal User user, @RequestParam Long productId, @RequestParam Integer quantity) {
        cartService.updateQuantity(user, productId, quantity);
        return ResponseEntity.ok("Quantity updated");
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotal(@AuthenticationPrincipal User user) {
        CartDTO cartDTO = cartService.getCartByUser(user);
        return ResponseEntity.ok(cartService.calculateTotal(cartDTO));
    }


}
