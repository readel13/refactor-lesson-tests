package example;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static example.Movie.MovieType.*;
import static org.junit.Assert.assertEquals;

public class CustomerTest {

    private static Movie TAXI;
    private static Movie KILL_BILL;
    private static Movie FAST_AND_FURIOUS;

    private final static String RESULT_TEMPLATE = """
            Rental Record for {CUSTOMER_NAME}
            {RENTALS}
            Amount owed is {TOTAL_AMOUNT}
            You earned {FREQUENT_RENTER_POINTS} frequent renter points""";

    @BeforeClass
    public static void init() {
        TAXI = new Movie("Taxi", REGULAR);
        KILL_BILL = new Movie("Kill Bill: Volume 1", NEW_RELEASE);
        FAST_AND_FURIOUS = new Movie("Fast and Furious", CHILDRENS);
    }

    @Test
    public void testAllMovieTypes() {
        var rentals = List.of(
                new Rental(TAXI, 5), new Rental(KILL_BILL, 3), new Rental(FAST_AND_FURIOUS, 8)
        );

        var rentalIntegerMap = Map.of(
                rentals.get(0), 6.5d,
                rentals.get(1), 9d,
                rentals.get(2), 9d
        );

        var testCustomer = new Customer("Test Customer", rentals);
        var expectedResult = buildStatementString(testCustomer.getName(), rentals, rentalIntegerMap, 24.5d, 4);

        assertEquals(expectedResult, testCustomer.statement());
    }

    @Test
    public void testRegularMovieTypes() {
        var rentals = List.of(
                new Rental(TAXI, 5), new Rental(TAXI, 10), new Rental(TAXI, 1)
        );

        var rentalIntegerMap = Map.of(
                rentals.get(0), 6.5d,
                rentals.get(1), 14.0d,
                rentals.get(2), 2.0d
        );

        var testCustomer = new Customer("Test Customer", rentals);
        var expectedResult = buildStatementString(testCustomer.getName(), rentals, rentalIntegerMap, 22.5d, 3);

        assertEquals(expectedResult, testCustomer.statement());
    }

    @Test
    public void testChildrenMovieTypes() {
        var rentals = List.of(
                new Rental(FAST_AND_FURIOUS, 5),
                new Rental(FAST_AND_FURIOUS, 2),
                new Rental(FAST_AND_FURIOUS, 3),
                new Rental(FAST_AND_FURIOUS, 7)
        );

        var rentalIntegerMap = Map.of(
                rentals.get(0), 4.5d,
                rentals.get(1), 1.5d,
                rentals.get(2), 1.5d,
                rentals.get(3), 7.5d
        );

        var testCustomer = new Customer("Test Customer", rentals);
        var expectedResult = buildStatementString(testCustomer.getName(), rentals, rentalIntegerMap, 15, 4);

        assertEquals(expectedResult, testCustomer.statement());
    }

    @Test
    public void testNewReleaseMovieTypes() {
        var rentals = List.of(
                new Rental(KILL_BILL, 5), new Rental(KILL_BILL, 2)
        );

        var rentalIntegerMap = Map.of(
                rentals.get(0), 15.0d,
                rentals.get(1), 6.0d
        );

        var testCustomer = new Customer("Test Customer", rentals);
        var expectedResult = buildStatementString(testCustomer.getName(), rentals, rentalIntegerMap, 21, 4);

        assertEquals(expectedResult, testCustomer.statement());
    }

    @Test
    public void testGetName() {
        String customerName = "Jong Deong";
        var customer = new Customer(customerName, Collections.emptyList());

        assertEquals(customerName, customer.getName());
    }


    private String buildStatementString(String customerName, List<Rental> rentals,
                                        Map<Rental, Double> rentalAmountMap, double totalAmount, int frequentRenterPoints) {
        return RESULT_TEMPLATE
                .replace("{CUSTOMER_NAME}", customerName)
                .replace("{RENTALS}", buildRentalsSection(rentals, rentalAmountMap))
                .replace("{TOTAL_AMOUNT}", String.valueOf(totalAmount))
                .replace("{FREQUENT_RENTER_POINTS}", String.valueOf(frequentRenterPoints));
    }

    private String buildRentalsSection(List<Rental> rentals, Map<Rental, Double> rentalAmountMap) {
        var result = new StringBuilder();

        for (Rental rental : rentals) {
            result.append("\t%s\t%s\n".formatted(rental.getMovie().getTitle(), rentalAmountMap.getOrDefault(rental, null)));
        }

        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }
}