import java.util.*;

// Reservation Class
class Reservation {
    private String reservationId;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

// Inventory Class
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public void increaseRoom(String type) {
        rooms.put(type, rooms.getOrDefault(type, 0) + 1);
    }

    public void display() {
        System.out.println("\nCurrent Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " -> " + rooms.get(type));
        }
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void add(Reservation r) {
        bookings.put(r.getReservationId(), r);
    }

    public Reservation get(String id) {
        return bookings.get(id);
    }

    public void remove(String id) {
        bookings.remove(id);
    }
}

// Cancellation Service
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancel(String reservationId, BookingHistory history, Inventory inventory) {

        // Validate
        Reservation r = history.get(reservationId);

        if (r == null) {
            System.out.println("Cancellation FAILED: Reservation not found");
            return;
        }

        // Push room ID to stack (LIFO rollback)
        rollbackStack.push(r.getRoomId());

        // Restore inventory
        inventory.increaseRoom(r.getRoomType());

        // Remove from history
        history.remove(reservationId);

        System.out.println("Cancellation SUCCESS for " + reservationId);
        System.out.println("Released Room ID: " + r.getRoomId());
    }

    // Optional: show rollback stack
    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main Class
public class usecase10 {

    public static void main(String[] args) {

        // Setup inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Double", 2);

        // Setup booking history (already confirmed bookings)
        BookingHistory history = new BookingHistory();

        history.add(new Reservation("R101", "Single", "SI101"));
        history.add(new Reservation("R102", "Double", "DO201"));

        // Cancellation service
        CancellationService service = new CancellationService();

        // Perform cancellations
        service.cancel("R101", history, inventory); // valid
        service.cancel("R999", history, inventory); // invalid

        // Show updated inventory
        inventory.display();

        // Show rollback stack
        service.showRollbackStack();
    }
}