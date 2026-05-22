package online.billmytrip.booking;

import jakarta.validation.Valid;
import online.billmytrip.pkg.CostBreakdown;
import online.billmytrip.pkg.PackageRequest;
import online.billmytrip.pkg.PricingService;
import online.billmytrip.user.User;
import online.billmytrip.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookings;
    private final UserRepository users;
    private final PricingService pricing;

    public BookingController(BookingRepository bookings, UserRepository users, PricingService pricing) {
        this.bookings = bookings;
        this.users = users;
        this.pricing = pricing;
    }

    @PostMapping
    public ResponseEntity<?> create(Authentication auth, @Valid @RequestBody PackageRequest req) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName()).orElseThrow();
        CostBreakdown cb = pricing.calculate(req);

        Booking b = new Booking();
        b.setUserId(u.getId());
        b.setDestinationIds(joinIds(req.destinationIds()));
        b.setDestinationNames(String.join(", ", cb.destinationNames()));
        b.setHotelType(req.hotelType().name());
        b.setTransportType(req.transportType().name());
        b.setActivityIds(req.activityIds() == null ? "" : joinIds(req.activityIds()));
        b.setActivityNames(String.join(", ", cb.activityNames()));
        b.setDays(req.days());
        b.setTravelers(req.travelers());
        b.setPromoCode(cb.promoApplied());
        b.setSubtotal(cb.subtotal());
        b.setDiscountAmount(cb.discountAmount());
        b.setTotalCost(cb.total());
        b.setStatus(BookingStatus.CONFIRMED);
        bookings.save(b);

        return ResponseEntity.ok(Map.of("booking", b, "breakdown", cb));
    }

    @GetMapping("/me")
    public ResponseEntity<?> mine(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(bookings.findByUserIdOrderByCreatedAtDesc(u.getId()));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(Authentication auth, @PathVariable Long id) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName()).orElseThrow();
        return bookings.findById(id).map(b -> {
            boolean isAdmin = u.getRole().name().equals("ADMIN");
            if (!isAdmin && !b.getUserId().equals(u.getId())) return ResponseEntity.status(403).build();
            b.setStatus(BookingStatus.CANCELLED);
            bookings.save(b);
            return ResponseEntity.ok(Map.of("ok", true));
        }).orElse(ResponseEntity.notFound().build());
    }

    private String joinIds(List<Long> ids) {
        return ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
