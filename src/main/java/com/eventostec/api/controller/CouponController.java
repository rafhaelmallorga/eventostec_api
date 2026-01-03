package com.eventostec.api.controller;


import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.service.CouponService;
import com.eventostec.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{event_id}")
    public ResponseEntity<Coupon> createCoupon(@PathVariable UUID event_id, @RequestBody CouponRequestDTO coupon) {
        CouponRequestDTO couponRequestDTO = new CouponRequestDTO(
                coupon.code(),
                coupon.discount(),
                coupon.valid()
        );
        Coupon newCoupon = this.couponService.createCoupon(event_id, couponRequestDTO);
        return ResponseEntity.ok(newCoupon);
    };
}
