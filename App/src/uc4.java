import java.util.*;

// Room Domain Class
class Room {
    private String roomType;
    private double price;
    private List<String> amenities;

    public Room(String roomType, double price, List<String> amenities) {
        this.roomType = roomType;
        this.price = price;
        this.amenities = amenities;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("---------------------------");
    }
}

// Inventory Class
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    // Read-only access
    public Map<String, Integer> getAvailability() {
        return roomAvailability;
    }
}

// Search Service
class SearchService {

    public void searchAvailableRooms(Inventory inventory, Map<String, Room> roomMap) {

        System.out.println("\nAvailable Rooms:\n");

        for (Map.Entry<String, Integer> entry : inventory.getAvailability().entrySet()) {

            String roomType = entry.getKey();
            int count = entry.getValue();

            // Show only available rooms
            if (count > 0 && roomMap.containsKey(roomType)) {

                Room room = roomMap.get(roomType);
                room.displayDetails();
                System.out.println("Available Count: " + count);
                System.out.println();
            }
        }
    }
}

// Main Class (FIXED NAME)
public class uc4 {

    public static void main(String[] args) {

        // Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);   // Should NOT be shown
        inventory.addRoom("Suite", 2);

        // Room details
        Map<String, Room> roomMap = new HashMap<>();

        roomMap.put("Single", new Room("Single", 2000,
                Arrays.asList("WiFi", "TV", "AC")));

        roomMap.put("Double", new Room("Double", 3500,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        roomMap.put("Suite", new Room("Suite", 7000,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar", "Jacuzzi")));

        // Search
        SearchService searchService = new SearchService();
        searchService.searchAvailableRooms(inventory, roomMap);
    }
}