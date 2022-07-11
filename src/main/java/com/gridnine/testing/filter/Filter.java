package com.gridnine.testing.filter;

import com.gridnine.testing.models.Flight;

import java.util.List;

/**
 * Функциональный интерфейс, который будет заниматься фильтрацией
 * набора перелётов согласно различным правилам
 */
@FunctionalInterface
public interface Filter {

    List<Flight> doFilter(List<Flight> flights);
}
