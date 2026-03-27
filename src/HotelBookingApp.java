import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * =========================================================================
 * MAIN CLASS - UseCase7AddOnServiceSelection
 * =========================================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * This class demonstrates how optional services can be attached to a confirmed
 * booking.
 *
 * Services are added after room allocation and do not affect inventory.
 *
 * @version 7.0
 */
public class HotelBookingApp {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Add-On Service Selection");

        // Initialize the Add-On Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Create optional services
        Service spaService = new Service("Spa", 1000.0);
        Service breakfastService = new Service("Breakfast", 500.0);

        // Simulated confirmed reservation ID
        String reservationId = "Single-1";

        // Attach services to the reservation
        serviceManager.addService(reservationId, spaService);
        serviceManager.addService(reservationId, breakfastService);

        // Display results
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost: " + serviceManager.calculateTotalServiceCost(reservationId));
    }
}

/**
 * =========================================================================
 * CLASS - Service
 * =========================================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * This class represents an optional service that can be added to a confirmed
 * reservation.
 *
 * Examples:
 * - Breakfast
 * - Spa
 * - Airport Pickup
 *
 * @version 7.0
 */
class Service {

    /** Name of the service. */
    private String serviceName;

    /** Cost of the service. */
    private double cost;

    /**
     * Creates a new add-on service.
     *
     * @param serviceName name of the service
     * @param cost cost of the service
     */
    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    /**
     * @return service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @return service cost
     */
    public double getCost() {
        return cost;
    }
}

/**
 * =========================================================================
 * CLASS - AddOnServiceManager
 * =========================================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * This class manages optional services associated with confirmed reservations.
 *
 * It supports attaching multiple services to a single reservation.
 *
 * @version 7.0
 */
class AddOnServiceManager {

    /**
     * Maps reservation ID to selected services.
     *
     * Key   -> Reservation ID
     * Value -> List of selected services
     */
    private Map<String, List<Service>> servicesByReservation;

    /**
     * Initializes the service manager.
     */
    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    /**
     * Attaches a service to a reservation.
     *
     * @param reservationId confirmed reservation ID
     * @param service add-on service
     */
    public void addService(String reservationId, Service service) {
        // computeIfAbsent ensures a new List is created if the reservationId doesn't exist yet
        servicesByReservation.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
    }

    /**
     * Calculates total add-on cost for a reservation.
     *
     * @param reservationId reservation ID
     * @return total service cost
     */
    public double calculateTotalServiceCost(String reservationId) {
        double totalCost = 0.0;
        List<Service> services = servicesByReservation.getOrDefault(reservationId, new ArrayList<>());

        for (Service service : services) {
            totalCost += service.getCost();
        }

        return totalCost;
    }
}