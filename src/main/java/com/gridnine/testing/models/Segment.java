package com.gridnine.testing.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Bean that represents a flight segment.
 */
@AllArgsConstructor
@Getter
public class Segment {
    private final LocalDateTime departureDate;
    private final LocalDateTime arrivalDate;

    @Override
    public String toString() {
        DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd 'T'HH:mm");
        return '[' + departureDate.format(fmt) + " | " + arrivalDate.format(fmt)
            + ']';
    }
}
