import java.util.*;

// Reservation Class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// Booking History (stores confirmed bookings)
class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation r) {
        history.add(r);
    }

    // Get all reservations (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    // Display full booking history
    public void showAllBookings(BookingHistory history) {
        System.out.println("\n--- Booking History ---\n");

        List<Reservation> list = history.getAllReservations();

        if (list.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : list) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummary(BookingHistory history) {
        System.out.println("\n--- Booking Summary Report ---\n");

        Map<String, Integer> countByRoomType = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            String type = r.getRoomType();
            countByRoomType.put(type, countByRoomType.getOrDefault(type, 0) + 1);
        }

        for (String type : countByRoomType.keySet()) {
            System.out.println(type + " Rooms Booked: " + countByRoomType.get(type));
        }
    }
}

// Main Class
public class uc8 {

    public static void main(String[] args) {

        // Booking History
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("R101", "Alice", "Single"));
        history.addReservation(new Reservation("R102", "Bob", "Double"));
        history.addReservation(new Reservation("R103", "Charlie", "Single"));
        history.addReservation(new Reservation("R104", "David", "Suite"));

        // Reporting
        BookingReportService reportService = new BookingReportService();

        // Admin views history
        reportService.showAllBookings(history);

        // Admin views summary
        reportService.generateSummary(history);
    }
}