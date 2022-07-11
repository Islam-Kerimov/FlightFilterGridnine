package com.gridnine.testing.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean that represents a flight.
 */
@AllArgsConstructor
@Getter
public class Flight {
    private final List<Segment> segments;

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
            .collect(Collectors.joining(" "));
    }
}