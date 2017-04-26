package com.taxiplon.taxi.driver.lit.HelperClasses;

import android.content.Context;

import com.taxiplon.taxi.driver.lit.Models.ZoneObject;
import com.taxiplon.taxi.driver.lit.Models.ZonePoints;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by makis on 1/5/2017.
 */

public class ZoneLocationHelper {

    private Double currentLat;
    private Double currentLng;
    private Context mContext;
    private MathHelperClass math;

    public ZoneLocationHelper(Context context, Double lat, Double lng) {
        this.mContext = context;
        this.currentLat = lat;
        this.currentLng = lng;
        math = new MathHelperClass(mContext);
    }

    public boolean insideZone(ZoneObject zone) {
        boolean isInsideZone = coordinateInsideZone(zone);
        return isInsideZone;
    }

    public boolean coordinateInsideZone(ZoneObject zone) {
        if(zone.isStrictBorders()){
            if(zone.getZonePoints()!=null && zone.getParsedZonePoints().size()>0) {
                List<ZonePoints> zonePoints = zone.getParsedZonePoints();
                if(zonePoints.size()>0) {
                    return isPointInPolygon(zonePoints);
                }
            } else if(BigDecimal.valueOf(zone.getZoneRange()).compareTo(BigDecimal.ZERO)!=0) {
                Double driverDistance = math.getDistanceInM(currentLat, currentLng, zone.getZoneLat(), zone.getZoneLong())/1000;
                return driverDistance<=zone.getZoneRange();
            }

        } else {
            return true;
        }
        return false;
    }

    public boolean isPointInPolygon(List<ZonePoints> zonePoints) {
        int intersectCount = 0;
        for(int j=0; j<zonePoints.size()-1; j++) {
            if( rayCastIntersect(currentLat,currentLng, zonePoints.get(j), zonePoints.get(j+1)) ) {
                intersectCount++;
            }
        }
        return (intersectCount%2) == 1; // odd = inside, even = outside;
    }

    public boolean shouldDisplayWarning(ZoneObject zone) {

        if(!zone.isStrictBorders()) {
            return false;
        }

        if(zone.getParsedZonePoints().size()>0) {
            return false;
        }

        Double driverDistance = math.getDistanceInM(currentLat, currentLng, zone.getZoneLat(), zone.getZoneLong())/1000;

        if(driverDistance>=(zone.getZoneRange()-0.3)) {
            return true;
        }

        return false;
    }

    private boolean rayCastIntersect(Double driverLat,Double driverLng, ZonePoints vertA, ZonePoints vertB) {

        double aY = vertA.getLatitude();
        //getLatitude();
        double bY = vertB.getLatitude();
        //getLatitude();
        double aX = vertA.getLongitude();
        //getLongitude();
        double bX = vertB.getLongitude();
        //getLongitude();
        double pY = driverLat;
        double pX = driverLng;

        if ( (aY>pY && bY>pY) || (aY<pY && bY<pY) || (aX<pX && bX<pX) ) {
            return false; // a and b can't both be above or below pt.y, and a or b must be east of pt.x
        }

        double m = (aY-bY) / (aX-bX);               // Rise over run
        double bee = (-aX) * m + aY;                // y = mx + b
        double x = (pY - bee) / m;                  // algebra is neat!

        return x > pX;
    }

}

