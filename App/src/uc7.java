import java.util.*;

// Add-On Service Class
class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public void display() {
        System.out.println(name + " - ₹" + cost);
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, Service service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added " + service.getName() + " to " + reservationId);
    }

    // Display services
    public void displayServices(String reservationId) {
        List<Service> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services added.");
            return;
        }

        System.out.println("\nServices for Reservation " + reservationId + ":");
        for (Service s : services) {
            s.display();
        }

        System.out.println("Total Cost: ₹" + calculateTotal(reservationId));
    }

    // Calculate total cost
    public double calculateTotal(String reservationId) {
        double total = 0;

        List<Service> services = serviceMap.get(reservationId);
        if (services != null) {
            for (Service s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

// Main Class
public class UC7 {

    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId = "RES101";

        // Available services
        Service wifi = new Service("WiFi", 200);
        Service breakfast = new Service("Breakfast", 500);
        Service spa = new Service("Spa", 1500);
        Service pickup = new Service("Airport Pickup", 1000);

        // Guest selects services
        manager.addService(reservationId, wifi);
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, spa);

        // Display selected services and total cost
        manager.displayServices(reservationId);
    }
}