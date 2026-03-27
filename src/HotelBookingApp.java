import java.util.HashMap;
import java.util.Map;


public class HotelBookingApp {

    public static void main(String[] args) {

        // Create Room objects (Domain Model)
        Room singleRoom = new Room("Single", 1, 250, 1500.0);
        Room doubleRoom = new Room("Double", 2, 400, 2500.0);
        Room suiteRoom = new Room("Suite", 3, 750, 5000.0);

        // Initialize inventory with availability
        RoomInventory inventory = new RoomInventory();
        inventory.setRoomAvailability("Single", 5);
        inventory.setRoomAvailability("Double", 3);
        inventory.setRoomAvailability("Suite", 2);

        // Create search service
        RoomSearchService searchService = new RoomSearchService();

        // Perform search (READ-ONLY)
        searchService.searchAvailableRooms(
                inventory,
                singleRoom,
                doubleRoom,
                suiteRoom
        );
    }
}

/**
 * ============================================================
 * CLASS - Room
 * ============================================================
 * Represents a room type with its details.
 */
class Room {
    private String type;
    private int beds;
    private int size;
    private double price;

    public Room(String type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public void displayDetails(int availability) {
        System.out.println(type + " Room:");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
        System.out.println("Available: " + availability);
        System.out.println();
    }
}

/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 * Holds room availability (STATE HOLDER).
 */
class RoomInventory {

    private Map<String, Integer> availability = new HashMap<>();

    public void setRoomAvailability(String type, int count) {
        availability.put(type, count);
    }

    public Map<String, Integer> getRoomAvailability() {
        return availability;
    }
}

/**
 * ============================================================
 * CLASS - RoomSearchService
 * ============================================================
 *
 * Provides read-only search functionality.
 */
class RoomSearchService {

    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        System.out.println("Room Search\n");

        Map<String, Integer> availability = inventory.getRoomAvailability();

        // Single Room
        if (availability.get("Single") != null && availability.get("Single") > 0) {
            singleRoom.displayDetails(availability.get("Single"));
        }

        // Double Room
        if (availability.get("Double") != null && availability.get("Double") > 0) {
            doubleRoom.displayDetails(availability.get("Double"));
        }

        // Suite Room
        if (availability.get("Suite") != null && availability.get("Suite") > 0) {
            suiteRoom.displayDetails(availability.get("Suite"));
        }
    }
}