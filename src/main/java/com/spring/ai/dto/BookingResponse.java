package com.spring.ai.dto;

import java.time.Instant;

import com.spring.ai.entity.BookingStatus;

public record BookingResponse(Long id, String destination, Instant departureTime, BookingStatus status) {}
