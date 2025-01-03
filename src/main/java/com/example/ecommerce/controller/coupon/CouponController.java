package com.example.ecommerce.controller.coupon;


import com.example.ecommerce.model.Cupon;
import com.example.ecommerce.service.CuponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/coupon")
public class CouponController {


    @Autowired
    private CuponService cuponService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cupon> createCoupon(@RequestBody Cupon coupon) {
        Cupon createCupon = cuponService.createCoupon(coupon);
        return ResponseEntity.ok(createCupon);
    }


    @PostMapping("/validate")
    public ResponseEntity<?> validateCoupon(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        try {
            Cupon coupon = cuponService.validateCoupon(code);
            Map<String, Object> response = new HashMap<>();
            response.put("discountPercentage", coupon.getDiscountPercentage());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
