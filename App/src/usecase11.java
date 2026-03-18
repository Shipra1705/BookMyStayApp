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

// Thread-safe Inventory
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {
        rooms.put("Single", 2);
    }

    // Synchronized method (critical section)
    public synchronized boolean allocateRoom(String type) {
        int available = rooms.getOrDefault(type, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " allocating room...");

            // simulate delay (to expose race condition if not synchronized)
            try { Thread.sleep(100); } catch (Exception e) {}

            rooms.put(type, available - 1);

            System.out.println(Thread.currentThread().getName() +
                    " SUCCESS booking. Remaining: " + (available - 1));

            return true;
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED (No rooms available)");
            return false;
        }
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private Queue<Reservation> queue;
    private Inventory inventory;

    public BookingProcessor(Queue<Reservation> queue, Inventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;

            // synchronized block for shared queue
            synchronized (queue) {
                if (queue.isEmpty()) {
                    break;
                }
                r = queue.poll();
            }

            // process booking
            inventory.allocateRoom(r.getRoomType());
        }
    }
}

// Main Class
public class usecase11 {

    public static void main(String[] args) {

        // Shared queue
        Queue<Reservation> queue = new LinkedList<>();

        // Add booking requests
        queue.add(new Reservation("Alice", "Single"));
        queue.add(new Reservation("Bob", "Single"));
        queue.add(new Reservation("Charlie", "Single"));
        queue.add(new Reservation("David", "Single"));

        // Shared inventory
        Inventory inventory = new Inventory();

        // Multiple threads (simulating concurrent users)
        Thread t1 = new BookingProcessor(queue, inventory);
        Thread t2 = new BookingProcessor(queue, inventory);
        Thread t3 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");
        t3.setName("Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();
    }
}