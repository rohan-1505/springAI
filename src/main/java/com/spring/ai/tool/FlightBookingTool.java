package com.spring.ai.tool;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.spring.ai.dto.BookingResponse;
import com.spring.ai.dto.BookingsListResponse;
import com.spring.ai.entity.BookingStatus;
import com.spring.ai.entity.FlightBooking;
import com.spring.ai.service.FlightBookingService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlightBookingTool {

    private final FlightBookingService flightBookingService;

    @Tool(
            name = "createBooking",
            description = "Create a new flight booking for a user"
    )
    public BookingResponse createBooking(

            @ToolParam(description = "The unique user id")
            String userId,

            @ToolParam(description = "The destination for the flight booking")
            String destination,

            @ToolParam(
                    description = "Departure date and time in ISO format like 2026-09-09T16:00:00"
            )
            LocalDateTime departureTime) {

        Instant departureInstant =
                departureTime.toInstant(ZoneOffset.UTC);

        var flightbooking =
                flightBookingService.createBooking(
                        userId,
                        destination,
                        departureInstant
                );

        return new BookingResponse(
                flightbooking.getId(),
                flightbooking.getDestination(),
                flightbooking.getDepartureTime(),
                flightbooking.getBookingStatus()
        );
    }


    @Tool(
            name = "get_user_bookings",
            description = "Retrieve all flight bookings for the current user, sorted by departure time (most recent first). Returns an empty list message if none exist."
    )
    public BookingsListResponse getUserBookings(

            @ToolParam(description = "The unique user ID")
            String userId

    ) {

        List<FlightBooking> bookings =
                flightBookingService.getUserBookings(userId);

        List<BookingResponse> responses =
                bookings.stream()
                        .map(b -> new BookingResponse(
                                b.getId(),
                                b.getDestination(),
                                b.getDepartureTime(),
                                b.getBookingStatus()
                        ))
                        .toList();

        String message = bookings.isEmpty()
                ? "You have no upcoming flight bookings."
                : "Here are your current flight bookings:";

        return new BookingsListResponse(
                responses,
                message
        );
    }


    @Tool(
            name = "update_booking_status",
            description = "Update the status of an existing flight booking. Only the owner of the booking can modify it. Use CANCELLED when the user wants to cancel a booking."
    )
    public BookingResponse updateBookingStatus(

            @ToolParam(
                    description = "The booking ID returned from createBooking or get_user_bookings",
                    required = true
            )
            Long bookingId,

            @ToolParam(
                    description = "The user ID who owns the booking",
                    required = true
            )
            String userId,

            @ToolParam(
                    description = "New status. Allowed values are CONFIRMED, CANCELLED, or PENDING",
                    required = true
            )
            BookingStatus newStatus

    ) {

        FlightBooking updated =
                flightBookingService.updateBookingStatus(
                        bookingId,
                        userId,
                        newStatus
                );

        return new BookingResponse(
                updated.getId(),
                updated.getDestination(),
                updated.getDepartureTime(),
                updated.getBookingStatus()
        );
    }
}