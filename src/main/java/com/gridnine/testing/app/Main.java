package com.gridnine.testing.app;

import com.gridnine.testing.filter.strategies.FlightDepartsBeforeArrivesFilter;
import com.gridnine.testing.filter.strategies.FlightDepartsUpToCurrentTimeFilter;
import com.gridnine.testing.filter.strategies.FlightWithMoreTwoHoursGroundTimeFilter;
import com.gridnine.testing.models.Flight;
import com.gridnine.testing.service.FlightService;
import com.gridnine.testing.utils.FlightBuilder;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        FlightService flightService = new FlightService(flights);

        // выводит на консоль результаты тестового набора перелетов без фильтров
        printFlight(flights, "RESULTS OF A TEST SET OF FLIGHTS WITHOUT FILTERS");

        // выводит на консоль результаты тестового набора перелетов
        // с исключением вылетов до текущего момента времени
        List<Flight> allWithoutDepartsUpToCurrentTime = flightService.executeFilter(new FlightDepartsUpToCurrentTimeFilter());
        printFlight(allWithoutDepartsUpToCurrentTime, "RESULTS OF A TEST SET OF FLIGHTS WITHOUT DEPARTURE UP TO THE CURRENT TIME");

        // выводит на консоль результаты тестового набора перелетов
        // с исключением перелетов с датой прилёта раньше даты вылета
        List<Flight> allWithoutArrivalBeforeDeparture = flightService.executeFilter(new FlightDepartsBeforeArrivesFilter());
        printFlight(allWithoutArrivalBeforeDeparture, "RESULTS OF A TEST SET OF FLIGHTS WITHOUT ARRIVAL BEFORE DEPARTURE");

        // выводит на консоль результаты тестового набора перелетов
        // с исключением перелетов с общим временем проведённым на земле превышающем два часа
        List<Flight> allWithOnGroundDurationNoMoreThanTwoHours = flightService.executeFilter(new FlightWithMoreTwoHoursGroundTimeFilter());
        printFlight(allWithOnGroundDurationNoMoreThanTwoHours, "RESULTS OF A TEST SET OF FLIGHTS WITH ON GROUND DURATION NO MORE THAN TWO HOURS");

        // all filters
        List<Flight> allFilters = flightService.executeFilter(new FlightDepartsUpToCurrentTimeFilter(),
                                                              new FlightDepartsBeforeArrivesFilter(),
                                                              new FlightWithMoreTwoHoursGroundTimeFilter());
        printFlight(allFilters, "ALL FILTERS");
    }

    private static void printFlight(List<Flight> flights, String data) {
        System.out.println("=============================================================" +
                               "============================================================");
        System.out.println(data);

        flights.forEach(flight -> System.out.println("- " + flight));

        System.out.println("=============================================================" +
                               "============================================================\n");
    }
}
