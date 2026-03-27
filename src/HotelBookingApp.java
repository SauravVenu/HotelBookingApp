import java.util.HashMap;
import java.util.Map;

/**
 * =========================================================================
 * MAIN CLASS - HotelBookingApp
 * =========================================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This class demonstrates fail-fast design by validating incoming booking
 * requests and throwing custom exceptions for invalid states.
 *
 * @version 9.0
 */
public class HotelBookingApp {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Error Handling & Validation");

        RoomInventory inventory = new RoomInventory();

        // Test Case 1: Invalid Room Type
        try {
            inventory.bookRoom("Penthouse");
        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Test Case 2: Valid Booking followed by Inventory Exhaustion
        try {
            // First booking should succeed (Inventory: 1 -> 0)
            inventory.bookRoom("Single");

            // Second booking should fail (Inventory: 0)
            inventory.bookRoom("Single");
        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/**
 * =========================================================================
 * CLASS - InvalidBookingException
 * =========================================================================
 *
 * Description:
 * A custom exception used to indicate invalid booking conditions such as
 * unrecognized room types or insufficient inventory.
 */
class InvalidBookingException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * =========================================================================
 * CLASS - BookingValidator
 * =========================================================================
 *
 * Description:
 * Validates booking inputs and system states.
 */
class BookingValidator {

    /**
     * Validates that the requested room type exists in the system.
     *
     * @param roomType the requested room type
     * @throws InvalidBookingException if room type is not recognized
     */
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!roomType.equals("Single") && !roomType.equals("Double") && !roomType.equals("Suite")) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    /**
     * Validates that inventory is sufficient for booking.
     *
     * @param currentInventory the current room count
     * @throws InvalidBookingException if inventory is 0 or less
     */
    public void validateInventory(int currentInventory) throws InvalidBookingException {
        if (currentInventory <= 0) {
            throw new InvalidBookingException("Inventory cannot be negative.");
        }
    }
}

/**
 * =========================================================================
 * CLASS - RoomInventory
 * =========================================================================
 *
 * Description:
 * Manages the available room counts and integrates validation before
 * allowing any state changes.
 */
class RoomInventory {

    /** Tracks available rooms by type. */
    private Map<String, Integer> availableRooms;

    /** Validator for checking booking rules. */
    private BookingValidator validator;

    /**
     * Initializes inventory with a small set of rooms to easily test exhaustion.
     */
    public RoomInventory() {
        availableRooms = new HashMap<>();
        // Starting with 1 Single room to easily trigger the negative inventory error
        availableRooms.put("Single", 1);
        availableRooms.put("Double", 2);
        availableRooms.put("Suite", 1);

        validator = new BookingValidator();
    }

    /**
     * Attempts to book a room safely by validating first.
     *
     * @param roomType the requested room type
     * @throws InvalidBookingException if validation fails
     */
    public void bookRoom(String roomType) throws InvalidBookingException {
        // 1. Validate Room Type first
        validator.validateRoomType(roomType);

        // 2. Check and Validate Inventory
        int currentCount = availableRooms.getOrDefault(roomType, 0);
        validator.validateInventory(currentCount);

        // 3. Update State (Safe to proceed)
        availableRooms.put(roomType, currentCount - 1);
        System.out.println("Booking successful for room type: " + roomType);
    }
}