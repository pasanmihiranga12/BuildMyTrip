package online.billmytrip.pkg;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PackageRequest(
        @NotEmpty List<Long> destinationIds,
        @NotNull HotelType hotelType,
        @NotNull TransportType transportType,
        List<Long> activityIds,
        @Min(1) int days,
        @Min(1) int travelers,
        String promoCode) {
}
