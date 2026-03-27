import java.util.ArrayList;
import java.util.List;

/**
 * =========================================================================
 * MAIN CLASS - HotelBookingApp
 * =========================================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class demonstrates how confirmed bookings are stored historically
 * to provide operational visibility and support reporting.
 *
 * It uses a list to maintain records in insertion order.
 *
 * @version 8.0
 */
public class HotelBookingApp {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Booking History & Reporting");

        // Initialize history and reporting services
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Create confirmed reservations
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // Store reservations in history
        history.addRecord(r1);
        history.addRecord(r2);
        history.addRecord(r3);

        // Generate and display summary report
        reportService.generateSummaryReport(history);
    }
}

/**
 * =========================================================================
 * CLASS - Reservation
 * =========================================================================
 *
 * Description:
 * This class represents a confirmed reservation.
 *
 * @version 8.0
 */
class Reservation {

    /** Name of the guest. */
    private String guestName;

    /** Room type assigned. */
    private String roomType;

    /**
     * Creates a new confirmed reservation.
     *
     * @param guestName name of the guest
     * @param roomType room type assigned
     */
    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    /** @return guest name */
    public String getGuestName() { return guestName; }

    /** @return room type */
    public String getRoomType() { return roomType; }
}

/**
 * =========================================================================
 * CLASS - BookingHistory
 * =========================================================================
 *
 * Description:
 * This class acts as a persistent record of all confirmed bookings.
 *
 * It uses a List to preserve the chronological order of reservations.
 *
 * @version 8.0
 */
class BookingHistory {

    /** List containing all past bookings. */
    private List<Reservation> pastBookings;

    /** Initializes an empty booking history. */
    public BookingHistory() {
        pastBookings = new ArrayList<>();
    }

    /**
     * Adds a confirmed reservation to the history.
     *
     * @param reservation confirmed reservation
     */
    public void addRecord(Reservation reservation) {
        pastBookings.add(reservation);
    }

    /**
     * Retrieves all stored booking records.
     *
     * @return list of past bookings
     */
    public List<Reservation> getAllRecords() {
        return pastBookings;
    }
}

/**
 * =========================================================================
 * CLASS - BookingReportService
 * =========================================================================
 *
 * Description:
 * This class generates reports based on stored booking history.
 *
 * It separates reporting logic from data storage, improving modularity.
 *
 * @version 8.0
 */
class BookingReportService {

    /**
     * Generates a summary report of all past bookings.
     *
     * @param history booking history repository
     */
    public void generateSummaryReport(BookingHistory history) {
        List<Reservation> records = history.getAllRecords();

        System.out.println("--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + records.size());

        for (Reservation res : records) {
            System.out.println("- Guest: " + res.getGuestName() +
                    ", Room Type: " + res.getRoomType());
        }

        System.out.println("------------------------------");
    }
}