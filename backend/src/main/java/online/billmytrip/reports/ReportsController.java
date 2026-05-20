package online.billmytrip.reports;

import online.billmytrip.booking.Booking;
import online.billmytrip.booking.BookingRepository;
import online.billmytrip.booking.BookingStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/reports")
public class ReportsController {

    private final BookingRepository bookings;

    public ReportsController(BookingRepository bookings) {
        this.bookings = bookings;
    }

    @GetMapping("/bookings")
    public Map<String, Object> bookingsReport() {
        List<Booking> all = bookings.findAll();
        long total = all.size();
        long confirmed = all.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED).count();
        long cancelled = total - confirmed;
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("totalBookings", total);
        r.put("confirmed", confirmed);
        r.put("cancelled", cancelled);
        r.put("recent", bookings.findAllByOrderByCreatedAtDesc().stream().limit(10).collect(Collectors.toList()));
        return r;
    }

    @GetMapping("/popular-destinations")
    public List<Map<String, Object>> popularDestinations() {
        Map<String, Long> counts = new HashMap<>();
        for (Booking b : bookings.findAll()) {
            if (b.getDestinationNames() == null || b.getDestinationNames().isBlank()) continue;
            for (String name : b.getDestinationNames().split(",\\s*")) {
                counts.merge(name, 1L, Long::sum);
            }
        }
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(e -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("destination", e.getKey());
                    m.put("bookings", e.getValue());
                    return m;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/revenue")
    public Map<String, Object> revenue() {
        BigDecimal confirmedRevenue = bookings.findAll().stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .map(Booking::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cancelledRevenue = bookings.findAll().stream()
                .filter(b -> b.getStatus() == BookingStatus.CANCELLED)
                .map(Booking::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("confirmedRevenue", confirmedRevenue);
        r.put("lostFromCancellations", cancelledRevenue);
        return r;
    }
}
