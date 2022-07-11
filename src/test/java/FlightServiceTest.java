import com.gridnine.testing.filter.strategies.FlightDepartsBeforeArrivesFilter;
import com.gridnine.testing.filter.strategies.FlightDepartsUpToCurrentTimeFilter;
import com.gridnine.testing.filter.strategies.FlightWithMoreTwoHoursGroundTimeFilter;
import com.gridnine.testing.models.Flight;
import com.gridnine.testing.models.Segment;
import com.gridnine.testing.service.FlightService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * todo Document type FilterTest
 */
public class FlightServiceTest {

    private static final LocalDateTime THREE_DAYS_FROM_NOW = LocalDateTime.now().plusDays(3);

    private static List<Flight> flights = new ArrayList<>();
    private static FlightService flightService;

    @BeforeAll
    static void init() {
        flights = asList(
            // [0] нормальный полет, с одним сегментом
            createFlight(THREE_DAYS_FROM_NOW, THREE_DAYS_FROM_NOW.plusHours(2)),
            // [1] нормальный полет, с одним сегментом
            createFlight(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(2)),
            // [2] полет с вылетом до текущего момента времени,с одним сегментом
            createFlight(THREE_DAYS_FROM_NOW.minusDays(6), THREE_DAYS_FROM_NOW),
            // [3] полет с вылетом до текущего момента времени, с одним сегментом
            createFlight(LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusHours(2)),
            // [4] полет с датой прилёта раньше даты вылета, с одним сегментом
            createFlight(THREE_DAYS_FROM_NOW, THREE_DAYS_FROM_NOW.minusHours(6)),
            // [5] полет с датой прилёта раньше даты вылета, с одним сегментом
            createFlight(THREE_DAYS_FROM_NOW, THREE_DAYS_FROM_NOW.minusSeconds(1)),
            // [6] полет с вылетом до текущего момента времени и с датой прилёта
            // раньше даты вылета, с одним сегментом
            createFlight(THREE_DAYS_FROM_NOW.minusDays(6), THREE_DAYS_FROM_NOW.minusDays(7)),
            // [7] полет с вылетом до текущего момента времени и с датой прилёта
            // раньше даты вылета, с одним сегментом
            createFlight(LocalDateTime.now().minusMinutes(1), LocalDateTime.now().minusMinutes(2)),
            // [8] нормальный полет, с двумя сегментами
            createFlight(THREE_DAYS_FROM_NOW, THREE_DAYS_FROM_NOW.plusHours(2),
                         THREE_DAYS_FROM_NOW.plusHours(3), THREE_DAYS_FROM_NOW.plusHours(5)),
            // [9] нормальный полет, с тремя сегментами
            createFlight(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(2),
                         LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5),
                         LocalDateTime.now().plusHours(5).plusMinutes(30), LocalDateTime.now().plusHours(8)),
            // [10] полет с вылетом до текущего момента времени, с двумя сегментами
            createFlight(THREE_DAYS_FROM_NOW.minusDays(6), THREE_DAYS_FROM_NOW.minusDays(6).plusHours(2),
                         THREE_DAYS_FROM_NOW.minusDays(6).plusHours(3), THREE_DAYS_FROM_NOW.minusDays(6).plusHours(5)),
            // [11] полет с вылетом до текущего момента времени, с тремя сегментами
            createFlight(THREE_DAYS_FROM_NOW.minusDays(5), THREE_DAYS_FROM_NOW.minusDays(5).plusHours(2),
                         THREE_DAYS_FROM_NOW.minusDays(5).minusDays(30), THREE_DAYS_FROM_NOW.minusDays(5).plusHours(4),
                         THREE_DAYS_FROM_NOW.minusDays(5).plusMinutes(30), THREE_DAYS_FROM_NOW.minusDays(5).plusHours(6)),
            // [12] полет с датой прилёта раньше даты вылета, с двумя сегментами
            createFlight(THREE_DAYS_FROM_NOW, THREE_DAYS_FROM_NOW.plusMinutes(30),
                         THREE_DAYS_FROM_NOW.plusHours(1), THREE_DAYS_FROM_NOW.plusMinutes(59)),
            // [13] полет с датой прилёта раньше даты вылета, с тремя сегментами
            createFlight(THREE_DAYS_FROM_NOW, THREE_DAYS_FROM_NOW.plusMinutes(30),
                         THREE_DAYS_FROM_NOW.plusHours(1), THREE_DAYS_FROM_NOW.plusMinutes(59),
                         THREE_DAYS_FROM_NOW.plusHours(2), THREE_DAYS_FROM_NOW.plusHours(1)),
            // [14] полет с общим временем проведенным на земле превышающим два часа,
            // с двумя сегментами
            createFlight(THREE_DAYS_FROM_NOW, THREE_DAYS_FROM_NOW.plusHours(2),
                         THREE_DAYS_FROM_NOW.plusHours(5), THREE_DAYS_FROM_NOW.plusHours(6)),
            // [15] полет с общим временем проведенным на земле превышающим два часа,
            // с тремя сегментами
            createFlight(THREE_DAYS_FROM_NOW, THREE_DAYS_FROM_NOW.plusHours(2),
                         THREE_DAYS_FROM_NOW.plusHours(3), THREE_DAYS_FROM_NOW.plusHours(4),
                         THREE_DAYS_FROM_NOW.plusHours(6), THREE_DAYS_FROM_NOW.plusHours(7)),
            // [16] полет с вылетом до текущего момента времени, с датой прилёта раньше даты вылета,
            // с общим временем проведенным на земле превышающим два часа, с двумя сегментами
            createFlight(LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(3),
                         LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(7))
        );

        flightService = new FlightService(flights);
    }

    @Test
    public void withoutFilter() {
        List<Flight> flightsExpected = flights;

        List<Flight> flightsActual = flightService.executeFilter();

        assertEquals(flightsExpected, flightsActual);
    }

    @Test
    public void departingInThePast() {
        List<Flight> flightsExpected = rightFlights(flights, new int[]{0, 1, 4, 5, 8, 9, 12, 13, 14, 15});

        List<Flight> flightsActual = flightService.executeFilter(new FlightDepartsUpToCurrentTimeFilter());

        assertEquals(flightsExpected, flightsActual);
    }

    @Test
    public void departsBeforeItArrives() {
        List<Flight> flightsExpected = rightFlights(flights, new int[]{0, 1, 2, 3, 8, 9, 10, 11, 14, 15});

        List<Flight> flightsActual = flightService.executeFilter(new FlightDepartsBeforeArrivesFilter());

        assertEquals(flightsExpected, flightsActual);
    }

    @Test
    public void moreThanTwoHoursGroundTime() {
        List<Flight> flightsExpected = rightFlights(flights, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13});

        List<Flight> flightsActual = flightService.executeFilter(new FlightWithMoreTwoHoursGroundTimeFilter());

        assertEquals(flightsExpected, flightsActual);
    }

    @Test
    public void departingInThePastAndDepartsBeforeItArrives() {
        List<Flight> flightsExpected = rightFlights(flights, new int[]{0, 1, 8, 9, 14, 15});

        List<Flight> flightsActual = flightService.executeFilter(new FlightDepartsUpToCurrentTimeFilter(),
                                                                 new FlightDepartsBeforeArrivesFilter());

        assertEquals(flightsExpected, flightsActual);
    }

    @Test
    public void departingInThePastAndMoreThanTwoHoursGroundTime() {
        List<Flight> flightsExpected = rightFlights(flights, new int[]{0, 1, 4, 5, 8, 9, 12, 13});

        List<Flight> flightsActual = flightService.executeFilter(new FlightDepartsUpToCurrentTimeFilter(),
                                                                 new FlightWithMoreTwoHoursGroundTimeFilter());

        assertEquals(flightsExpected, flightsActual);
    }

    @Test
    public void departsBeforeItArrivesAndMoreThanTwoHoursGroundTime() {
        List<Flight> flightsExpected = rightFlights(flights, new int[]{0, 1, 2, 3, 8, 9, 10, 11});

        List<Flight> flightsActual = flightService.executeFilter(new FlightDepartsBeforeArrivesFilter(),
                                                                 new FlightWithMoreTwoHoursGroundTimeFilter());

        assertEquals(flightsExpected, flightsActual);
    }

    @Test
    public void allFilters() {
        List<Flight> flightsExpected = rightFlights(flights, new int[]{0, 1, 8, 9});

        List<Flight> flightsActual = flightService.executeFilter(new FlightDepartsUpToCurrentTimeFilter(),
                                                                 new FlightDepartsBeforeArrivesFilter(),
                                                                 new FlightWithMoreTwoHoursGroundTimeFilter());

        assertEquals(flightsExpected, flightsActual);
    }

    private List<Flight> rightFlights(List<Flight> flights, int[] ints) {
        List<Flight> flightsExpected = new ArrayList<>();
        for (int i : ints) {
            flightsExpected.add(flights.get(i));
        }

        return flightsExpected;
    }

    private static Flight createFlight(final LocalDateTime... dates) {
        if ((dates.length % 2) != 0) {
            throw new IllegalArgumentException("you must pass an even number of dates");
        }
        List<Segment> segments = new ArrayList<>(dates.length / 2);
        for (int i = 0; i < (dates.length - 1); i += 2) {
            segments.add(new Segment(dates[i], dates[i + 1]));
        }
        return new Flight(segments);
    }
}
