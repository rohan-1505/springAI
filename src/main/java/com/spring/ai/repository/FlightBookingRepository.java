package com.spring.ai.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.ai.entity.FlightBooking;

import java.time.Instant;
import java.util.List;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {

    List<FlightBooking> findByUserIdOrderByDepartureTimeDesc(String userId);

    boolean existsByUserIdAndDestinationAndDepartureTime(
            String userId, String destination, Instant departureTime);

}