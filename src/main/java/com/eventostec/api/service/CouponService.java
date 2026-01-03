package com.eventostec.api.service;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.repositories.CouponRepository;
import com.eventostec.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventService eventService;

    public Coupon createCoupon(UUID event_id, CouponRequestDTO data) {
        Event eventFound = this.eventService.getEvent(event_id);

        Coupon newCouponDTO = new Coupon();
        newCouponDTO.setCode(data.code());
        newCouponDTO.setDiscount(data.discount());
        newCouponDTO.setValid(new Date(data.valid()));
        newCouponDTO.setEvent(eventFound);

        return this.couponRepository.save(newCouponDTO);
    }

}
