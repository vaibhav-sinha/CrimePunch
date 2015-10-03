package com.crimepunch.app.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by user-1 on 3/10/15.
 */
@Data
public class Location {

    private BigDecimal latitude;
    private BigDecimal longitude;

    public Location(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
