import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Class
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public boolean isValidRoomType(String type) {
        return rooms.containsKey(type);
    }

    public int getAvailability(String type) {
        return rooms.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) throws InvalidBookingException {
        int current = rooms.get(type);

        if (current <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }

        rooms.put(type, current - 1);
    }
}

// Validator Class
class BookingValidator {

    public void validate(Reservation r, Inventory inventory) throws InvalidBookingException {

        // Check null/empty
        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (r.getRoomType() == null || r.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        // Validate room type
        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available for type: " + r.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    private BookingValidator validator = new BookingValidator();

    public void processBooking(Reservation r, Inventory inventory) {

        try {
            // Validate first (Fail-Fast)
            validator.validate(r, inventory);

            // Reduce inventory (safe)
            inventory.reduceRoom(r.getRoomType());

            // Confirm booking
            System.out.println("Booking SUCCESS for " + r.getGuestName() +
                    " | Room: " + r.getRoomType());

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking FAILED: " + e.getMessage());
        }
    }
}

// Main Class (matches file name)
public class usecase9 {

    public static void main(String[] args) {

        Inventory inventory = new Inventory();

        // Setup rooms
        inventory.addRoom("Single", 1);
        inventory.addRoom("Double", 0);

        BookingService service = new BookingService();

        // Test cases
        Reservation r1 = new Reservation("Alice", "Single");   // valid
        Reservation r2 = new Reservation("Bob", "Double");     // no availability
        Reservation r3 = new Reservation("", "Single");        // invalid name
        Reservation r4 = new Reservation("David", "Suite");    // invalid type

        service.processBooking(r1, inventory);
        service.processBooking(r2, inventory);
        service.processBooking(r3, inventory);
        service.processBooking(r4, inventory);
    }
}