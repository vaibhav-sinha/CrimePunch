package com.crimepunch.app.event;

import com.crimepunch.app.base.BaseEvent;
import com.crimepunch.app.model.CrimeType;
import com.crimepunch.app.model.GridPoint;
import com.crimepunch.app.model.PointEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user-1 on 3/10/15.
 */
@Data
public class LocationDataUpdateEvent extends BaseEvent {
    List<GridPoint> gridPoints = new ArrayList<>();
    List<PointEntity> interestingPoints = new ArrayList<>();
    List<CrimeType> crimeTypes = new ArrayList<>();
}
