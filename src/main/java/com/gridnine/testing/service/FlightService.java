package com.gridnine.testing.service;

import com.gridnine.testing.filter.Filter;
import com.gridnine.testing.models.Flight;

import java.util.ArrayList;
import java.util.List;

public class FlightService {

    private final List<Flight> startFlights;

    public FlightService(List<Flight> startFlights) {
        this.startFlights = new ArrayList<>();
        this.startFlights.addAll(startFlights);
    }

    public List<Flight> executeFilter(Filter... filters) {
        List<Flight> flights = new ArrayList<>(startFlights);
        for (Filter filter : filters) {
            flights = filter.doFilter(flights);
        }

        return flights;
    }
}