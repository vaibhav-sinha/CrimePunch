package com.crimepunch.app.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user-1 on 3/10/15.
 */
@Data
public class LocationUpdateResponse {
    List<GridPoint> gridPoints = new ArrayList<>();
    List<PointEntity> interestingPoints = new ArrayList<>();
    List<CrimeType> crimeTypes = new ArrayList<>();
}
