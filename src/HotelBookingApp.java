import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * =========================================================================
 * MAIN CLASS - HotelBookingApp
 * =========================================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class demonstrates how confirmed bookings can be safely cancelled.
 * It uses a Stack to track cancelled room IDs for LIFO rollback operations,
 * and ensures inventory is accurately restored.
 *
 * @version 10.0
 */
public class HotelBookingApp {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Booking Cancellation & Inventory Rollback");

        // Initialize inventory and cancellation service
        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        // Test Case 1: Valid cancellation
        String validBookingId = "Single-1";
        cancellationService.cancelBooking(validBookingId, "Single", inventory);

        // Test Case 2: Invalid cancellation
        String invalidBookingId = "Invalid-99";
        cancellationService.cancelBooking(invalidBookingId, "Single", inventory);
    }
}

/**
 * =========================================================================
 * CLASS - CancellationService
 * =========================================================================
 *
 * Description:
 * This class handles the safe cancellation of bookings.
 * It uses a Stack to model the LIFO (Last-In-First-Out) rollback behavior.
 */
class CancellationService {

    /** Tracks recently released room IDs. */
    private Stack<String> cancelledRooms;

    /** Initializes the cancellation service. */
    public CancellationService() {
        cancelledRooms = new Stack<>();
    }

    /**
     * Cancels a booking, adds it to the rollback stack, and restores inventory.
     *
     * @param bookingId the unique ID of the booking to cancel
     * @param roomType the type of room being released
     * @param inventory the centralized room inventory
     */
    public void cancelBooking(String bookingId, String roomType, RoomInventory inventory) {
        if (isValidBooking(bookingId)) {
            System.out.println("Cancelling Booking ID: " + bookingId);

            // Push to rollback stack
            cancelledRooms.push(bookingId);
            System.out.println("Room " + bookingId + " has been added to the rollback stack.");

            // Restore inventory
            inventory.incrementInventory(roomType);
        } else {
            System.out.println("Cancellation failed: Invalid Booking ID");
        }
    }

    /**
     * Validates if the booking ID exists and is cancellable.
     * * @param bookingId the booking ID to check
     * @return true if valid, false otherwise
     */
    private boolean isValidBooking(String bookingId) {
        // Mock validation: In a real system, this would check a database or map of active bookings.
        // For this demonstration, we assume "Single-1" is our only active, valid booking.
        return "Single-1".equals(bookingId);
    }
}

/**
 * =========================================================================
 * CLASS - RoomInventory
 * =========================================================================
 *
 * Description:
 * Manages the available room counts. Provides methods to safely increment
 * the inventory when a room is released/cancelled.
 */
class RoomInventory {

    /** Tracks available rooms by type. */
    private Map<String, Integer> availableRooms;

    /**
     * Initializes inventory with a baseline count.
     */
    public RoomInventory() {
        availableRooms = new HashMap<>();
        availableRooms.put("Single", 5);
        availableRooms.put("Double", 5);
        availableRooms.put("Suite", 2);
    }

    /**
     * Restores inventory for a specific room type upon cancellation.
     *
     * @param roomType the requested room type
     */
    public void incrementInventory(String roomType) {
        int currentCount = availableRooms.getOrDefault(roomType, 0);
        availableRooms.put(roomType, currentCount + 1);
        System.out.println("Restored inventory for room type: " + roomType);
    }

    /**
     * Gets the current availability count for a room type.
     * * @param roomType the type of room
     * @return the available count
     */
    public int getAvailableCount(String roomType) {
        return availableRooms.getOrDefault(roomType, 0);
    }
}