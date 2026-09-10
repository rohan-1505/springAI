package com.spring.ai.dto;

import java.util.List;

public record BookingsListResponse(List<BookingResponse> bookings, String message) {}

