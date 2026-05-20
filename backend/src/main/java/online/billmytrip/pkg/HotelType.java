package online.billmytrip.pkg;

import java.math.BigDecimal;

public enum HotelType {
    BUDGET(new BigDecimal("1.00")),
    STANDARD(new BigDecimal("1.50")),
    LUXURY(new BigDecimal("2.50"));

    public final BigDecimal multiplier;
    HotelType(BigDecimal multiplier) { this.multiplier = multiplier; }
}
