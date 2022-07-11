package com.gridnine.testing.filter.strategies;

import com.gridnine.testing.filter.Filter;
import com.gridnine.testing.models.Flight;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Реализация интерфейса Filter исключающая из набора полетов
 * те полеты, которые имеют сегменты с датой прилета раньше даты вылета
 */
public class FlightDepartsBeforeArrivesFilter implements Filter {
    /**
     * @param flights список всех перелетов
     * @return отфильтрованный список перелетов (с датой вылета раньше даты прилета)
     */
    @Override
    public List<Flight> doFilter(List<Flight> flights) {

        return flights.stream()
            .filter(flight -> flight.getSegments().stream()
                .noneMatch(date -> date.getArrivalDate().isBefore(date.getDepartureDate())))
            .collect(toList());
    }
}
