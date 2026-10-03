import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * =========================================================================
 * MAIN CLASS - HotelBookingAPP
 * =========================================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class demonstrates how to save application state to a file using
 * serialization, and how to recover it upon system restart using deserialization.
 *
 * @version 12.0
 */
public class HotelBookingApp {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Data Persistence & System Recovery\n");
        String filename = "inventory.dat";

        // ==========================================
        // STEP 1: Initialize System and Modify State
        // ==========================================
        System.out.println("--- System Running ---");
        RoomInventory inventory1 = new RoomInventory();
        System.out.println("Initial Single Rooms: " + inventory1.getAvailableCount("Single"));

        // Modify state by booking a room
        inventory1.bookRoom("Single");

        // ==========================================
        // STEP 2: Save State and Shut Down
        // ==========================================
        PersistenceService.saveState(inventory1, filename);
        System.out.println("System shutting down...\n");

        // ==========================================
        // STEP 3: System Restart and Recovery
        // ==========================================
        System.out.println("--- System Restarting ---");

        // Attempt to load the previously saved state into a NEW object
        RoomInventory inventory2 = PersistenceService.loadState(filename);

        // Verify that the modified state was successfully recovered
        if (inventory2 != null) {
            System.out.println("Recovered Single Rooms: " + inventory2.getAvailableCount("Single"));
        } else {
            System.out.println("Failed to recover inventory state. Starting fresh.");
        }
    }
}

/**
 * =========================================================================
 * CLASS - RoomInventory
 * =========================================================================
 *
 * Description:
 * Manages the available room counts.
 * MUST implement Serializable to allow its state to be written to a file.
 */
class RoomInventory implements Serializable {

    // Recommended for Serializable classes to verify version compatibility
    private static final long serialVersionUID = 1L;

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
     * Books a room and decrements inventory.
     *
     * @param roomType the requested room type
     */
    public void bookRoom(String roomType) {
        int currentCount = availableRooms.getOrDefault(roomType, 0);
        if (currentCount > 0) {
            availableRooms.put(roomType, currentCount - 1);
            System.out.println("Booking successful. Decreased inventory for: " + roomType);
        } else {
            System.out.println("No " + roomType + " rooms available to book.");
        }
    }

    /**
     * Gets the current availability count for a room type.
     *
     * @param roomType the type of room
     * @return the available count
     */
    public int getAvailableCount(String roomType) {
        return availableRooms.getOrDefault(roomType, 0);
    }
}

/**
 * =========================================================================
 * CLASS - PersistenceService
 * =========================================================================
 *
 * Description:
 * Handles storing and retrieving system state from persistent storage
 * using Java's built-in Object streams.
 */
class PersistenceService {

    /**
     * Serializes the RoomInventory object and writes it to a file.
     *
     * @param inventory the current state to save
     * @param filename the destination file
     */
    public static void saveState(RoomInventory inventory, String filename) {
        // try-with-resources automatically closes the output streams
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(inventory);
            System.out.println("System state successfully saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    /**
     * Reads a file and deserializes it back into a RoomInventory object.
     *
     * @param filename the file to read from
     * @return the recovered RoomInventory object, or null if it fails
     */
    public static RoomInventory loadState(String filename) {
        // try-with-resources automatically closes the input streams
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            System.out.println("System state successfully recovered from " + filename);
            return inventory;
        } catch (FileNotFoundException e) {
            System.out.println("No previous state file found. A new system state will be initialized.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
        }
        return null; // Return null so the main app knows recovery failed
    }
}