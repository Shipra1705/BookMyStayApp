import java.util.*;

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

// Inventory Service
class Inventory {
    private Map<String, Integer> availability;

    public Inventory() {
        availability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// Booking Service (Core Logic)
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeToIds = new HashMap<>();

    // Process queue and allocate rooms
    public void processBookings(Queue<Reservation> queue, Inventory inventory) {

        while (!queue.isEmpty()) {

            Reservation r = queue.poll(); // FIFO
            String type = r.getRoomType();

            System.out.println("\nProcessing request for " + r.getGuestName());

            // Check availability
            if (inventory.getAvailable(type) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(type);

                // Ensure uniqueness
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = generateRoomId(type);
                }

                // Store in Set
                allocatedRoomIds.add(roomId);

                // Map room type → IDs
                roomTypeToIds
                        .computeIfAbsent(type, k -> new HashSet<>())
                        .add(roomId);

                // Reduce inventory
                inventory.reduceRoom(type);

                // Confirm booking
                System.out.println("Booking CONFIRMED for " + r.getGuestName());
                System.out.println("Room Type: " + type);
                System.out.println("Allocated Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + r.getGuestName() + " (No rooms available)");
            }
        }
    }

    // Generate Room ID
    private String generateRoomId(String type) {
        return type.substring(0, 2).toUpperCase() + new Random().nextInt(1000);
    }
}

// Main Class
public class uc6 {

    public static void main(String[] args) {

        // Step 1: Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);
        inventory.addRoom("Suite", 1);

        // Step 2: Create Booking Queue (FIFO)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single"));
        queue.offer(new Reservation("Bob", "Single"));
        queue.offer(new Reservation("Charlie", "Single")); // should fail
        queue.offer(new Reservation("David", "Suite"));

        // Step 3: Process Bookings
        BookingService service = new BookingService();
        service.processBookings(queue, inventory);
    }
}