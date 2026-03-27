import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * =========================================================================
 * MAIN CLASS - UseCase6RoomAllocationService
 * =========================================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class demonstrates how booking requests are confirmed and rooms
 * are allocated safely.
 *
 * It consumes booking requests in FIFO order and updates inventory immediately.
 *
 * @version 6.0
 */
public class HotelBookingApp {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        // Initialize queue, inventory, and allocation service
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Create booking requests (Note: Subha is set to "Single" to match expected output)
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));

        // Process requests in FIFO order
        while (bookingQueue.hasPendingRequests()) {
            Reservation currentRequest = bookingQueue.getNextRequest();
            allocationService.allocateRoom(currentRequest, inventory);
        }
    }
}

/**
 * =========================================================================
 * CLASS - RoomAllocationService
 * =========================================================================
 *
 * Description:
 * This class is responsible for confirming booking requests and assigning rooms.
 *
 * It ensures:
 * - Each room ID is unique
 * - Inventory is updated immediately
 * - No room is double-booked
 *
 * @version 6.0
 */
class RoomAllocationService {

    /**
     * Stores all allocated room IDs to
     * prevent duplicate assignments.
     */
    private Set<String> allocatedRoomIds;

    /**
     * Stores assigned room IDs by room type.
     *
     * Key   -> Room type
     * Value -> Set of assigned room IDs
     */
    private Map<String, Set<String>> assignedRoomsByType;

    /**
     * Initializes allocation tracking structures.
     */
    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms a booking request by assigning
     * a unique room ID and updating inventory.
     *
     * @param reservation booking request
     * @param inventory centralized room inventory
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();

        // Check availability
        if (inventory.isAvailable(roomType)) {
            // Generate unique Room ID
            String roomId = generateRoomId(roomType);

            // Record the room ID to ensure uniqueness
            allocatedRoomIds.add(roomId);
            assignedRoomsByType.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

            // Immediately decrement the inventory
            inventory.decrementInventory(roomType);

            // Confirm the reservation
            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() +
                    ", Room ID: " + roomId);
        } else {
            System.out.println("Booking failed for Guest: " + reservation.getGuestName() +
                    " - No " + roomType + " rooms available.");
        }
    }

    /**
     * Generates a unique room ID
     * for the given room type.
     *
     * @param roomType type of room
     * @return unique room ID
     */
    private String generateRoomId(String roomType) {
        int nextIdNumber = 1;
        if (assignedRoomsByType.containsKey(roomType)) {
            nextIdNumber = assignedRoomsByType.get(roomType).size() + 1;
        }
        return roomType + "-" + nextIdNumber;
    }
}

/**
 * =========================================================================
 * CLASS - RoomInventory (Mock for Use Case 6)
 * =========================================================================
 * Description: Manages the count of available rooms.
 */
class RoomInventory {
    private Map<String, Integer> availableRooms;

    public RoomInventory() {
        availableRooms = new HashMap<>();
        // Pre-populate with some inventory capacity
        availableRooms.put("Single", 5);
        availableRooms.put("Double", 5);
        availableRooms.put("Suite", 2);
    }

    public boolean isAvailable(String roomType) {
        return availableRooms.getOrDefault(roomType, 0) > 0;
    }

    public void decrementInventory(String roomType) {
        if (isAvailable(roomType)) {
            availableRooms.put(roomType, availableRooms.get(roomType) - 1);
        }
    }
}

/**
 * =========================================================================
 * CLASS - Reservation (From Use Case 5)
 * =========================================================================
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

/**
 * =========================================================================
 * CLASS - BookingRequestQueue (From Use Case 5)
 * =========================================================================
 */
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}