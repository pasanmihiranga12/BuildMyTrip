package online.billmytrip.pkg;

import java.math.BigDecimal;
import java.util.List;

public record CostBreakdown(
        BigDecimal destinationsCost,
        BigDecimal hotelCost,
        BigDecimal transportCost,
        BigDecimal activitiesCost,
        BigDecimal subtotal,
        int discountPercent,
        BigDecimal discountAmount,
        BigDecimal total,
        List<String> destinationNames,
        List<String> activityNames,
        String promoApplied) {
}
