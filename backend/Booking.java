package online.billmytrip.booking;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "destination_ids", nullable = false, length = 500)
    private String destinationIds;

    @Column(name = "destination_names", length = 1000)
    private String destinationNames;

    @Column(name = "hotel_type", nullable = false, length = 20)
    private String hotelType;

    @Column(name = "transport_type", nullable = false, length = 20)
    private String transportType;

    @Column(name = "activity_ids", length = 500)
    private String activityIds;

    @Column(name = "activity_names", length = 1000)
    private String activityNames;

    @Column(nullable = false)
    private int days;

    @Column(nullable = false)
    private int travelers;

    @Column(name = "promo_code", length = 40)
    private String promoCode;

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "total_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDestinationIds() { return destinationIds; }
    public void setDestinationIds(String destinationIds) { this.destinationIds = destinationIds; }
    public String getDestinationNames() { return destinationNames; }
    public void setDestinationNames(String destinationNames) { this.destinationNames = destinationNames; }
    public String getHotelType() { return hotelType; }
    public void setHotelType(String hotelType) { this.hotelType = hotelType; }
    public String getTransportType() { return transportType; }
    public void setTransportType(String transportType) { this.transportType = transportType; }
    public String getActivityIds() { return activityIds; }
    public void setActivityIds(String activityIds) { this.activityIds = activityIds; }
    public String getActivityNames() { return activityNames; }
    public void setActivityNames(String activityNames) { this.activityNames = activityNames; }
    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }
    public int getTravelers() { return travelers; }
    public void setTravelers(int travelers) { this.travelers = travelers; }
    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
