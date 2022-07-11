package com.gridnine.testing.filter.strategies;

import com.gridnine.testing.filter.Filter;
import com.gridnine.testing.models.Flight;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Реализация интерфейса Filter исключающая из набора полетов
 * те перелеты, вылет которых совершен до текущего момента времени
 */
public class FlightDepartsUpToCurrentTimeFilter implements Filter {

    private static final LocalDateTime CURRENT_DATE = LocalDateTime.now();

    /**
     * @param flights список всех перелетов
     * @return отфильтрованный список перелетов (с вылетом позже текущего момента времени)
     */
    @Override
    public List<Flight> doFilter(List<Flight> flights) {

        return flights.stream()
            .filter(flight -> flight.getSegments().stream()
                .noneMatch(date -> date.getDepartureDate().isBefore(CURRENT_DATE)))
            .collect(toList());
    }
}
