package online.billmytrip.admin;

import online.billmytrip.booking.Booking;
import online.billmytrip.booking.BookingRepository;
import online.billmytrip.user.User;
import online.billmytrip.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository users;
    private final BookingRepository bookings;

    public AdminController(UserRepository users, BookingRepository bookings) {
        this.users = users;
        this.bookings = bookings;
    }

    @GetMapping("/users")
    public List<Map<String, Object>> listUsers() {
        return users.findAll().stream().map(u -> {
            Map<String, Object> m = new java.util.LinkedHashMap<>();
            m.put("id", u.getId());
            m.put("username", u.getUsername());
            m.put("email", u.getEmail());
            m.put("phone", u.getPhone());
            m.put("role", u.getRole().name());
            return m;
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return users.findById(id).map(u -> {
            if (u.getRole() == online.billmytrip.user.Role.ADMIN) {
                return ResponseEntity.badRequest().body(Map.of("error", "cannot delete admin user"));
            }
            users.deleteById(id);
            return ResponseEntity.ok(Map.of("ok", true));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bookings")
    public List<Booking> listBookings() {
        return bookings.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<Booking> oneBooking(@PathVariable Long id) {
        return bookings.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
