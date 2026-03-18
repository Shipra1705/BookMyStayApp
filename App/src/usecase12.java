import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return reservationId + " -> " + roomType;
    }
}

// Inventory (Serializable)
class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

    public void display() {
        System.out.println("Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " : " + rooms.get(type));
        }
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> bookings = new ArrayList<>();

    public void add(Reservation r) {
        bookings.add(r);
    }

    public List<Reservation> getAll() {
        return bookings;
    }

    public void display() {
        System.out.println("Booking History:");
        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }
}

// Wrapper class to persist whole system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Inventory inventory;
    BookingHistory history;

    public SystemState(Inventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_data.ser";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("\nData saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) in.readObject();
            System.out.println("\nData loaded successfully!");
            return state;

        } catch (Exception e) {
            System.out.println("\nNo previous data found. Starting fresh...");
            return null;
        }
    }
}

// Main Class
public class usecase12 {

    public static void main(String[] args) {

        // Try loading previous state
        SystemState state = PersistenceService.load();

        Inventory inventory;
        BookingHistory history;

        if (state != null) {
            inventory = state.inventory;
            history = state.history;
        } else {
            // Fresh start
            inventory = new Inventory();
            inventory.addRoom("Single", 2);
            inventory.addRoom("Double", 1);

            history = new BookingHistory();

            history.add(new Reservation("R101", "Single"));
            history.add(new Reservation("R102", "Double"));
        }

        // Display current state
        System.out.println("\n--- CURRENT SYSTEM STATE ---");
        inventory.display();
        history.display();

        // Save state before exit
        PersistenceService.save(new SystemState(inventory, history));
    }
}