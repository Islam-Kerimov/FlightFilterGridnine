package com.gridnine.testing.filter.strategies;

import com.gridnine.testing.filter.Filter;
import com.gridnine.testing.models.Flight;
import com.gridnine.testing.models.Segment;

import java.util.List;

import static java.time.Duration.between;
import static java.util.stream.Collectors.toList;

/**
 * Реализация интерфейса Filter исключающая из набора полетов
 * те перелеты, общее время, проведенное на земле превышает два часа
 */
public class FlightWithMoreTwoHoursGroundTimeFilter implements Filter {
    private static final Integer TWO_HOURS = 7200;

    /**
     * @param flights список всех перелетов
     * @return отфильтрованный список перелетов
     * (общее время проведенное на земле между рейсами не более двух часов)
     */
    @Override
    public List<Flight> doFilter(List<Flight> flights) {

        return flights.stream()
            .filter(flight -> flight.getSegments().size() == 1
                || HoursInGroundLessTwo(flight.getSegments()))
            .collect(toList());
    }

    private boolean HoursInGroundLessTwo(List<Segment> segments) {
        long secondsInGround = 0;
        for (int i = 1; i != segments.size(); i++) {
            secondsInGround += between(
                segments.get(i - 1).getArrivalDate(),
                segments.get(i).getDepartureDate()
            ).getSeconds();
        }
        return secondsInGround <= TWO_HOURS;
    }
}
