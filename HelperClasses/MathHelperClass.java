
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by makis on 12/21/2016.
 */

public class MathHelperClass {

    private Context mContext;
    public static final double MICRODEGREES_COEFF = 1E6;

    public MathHelperClass(Context context){
        this.mContext = context;
    }

    public double getDistanceInM(double fromLat, double fromLng, double toLat, double toLng) {
        return getDistanceInKm(fromLat, fromLng, toLat, toLng) * 1000;
    }

    public double getDistance(int fromMicroLat, int fromMicroLng, int toMicroLat, int toMicroLng) {
        return getDistanceInKm(microdegreesToDegrees(fromMicroLat), microdegreesToDegrees(fromMicroLng), microdegreesToDegrees(toMicroLat), microdegreesToDegrees(toMicroLng));
    }

    public double getDistanceInKm(double fromLat, double fromLng, double toLat, double toLng) {
        return 6371*2*Math.atan2(Math.sqrt(Math.pow(Math.sin(Math.toRadians(toLat - fromLat)/2), 2) + Math.pow(Math.sin(Math.toRadians(toLng - fromLng)/2), 2) * Math.cos(Math.toRadians(fromLat)) * Math.cos(Math.toRadians(toLat))), Math.sqrt(1 - Math.pow(Math.sin(Math.toRadians(toLat - fromLat)/2), 2) + Math.pow(Math.sin(Math.toRadians(toLng - fromLng)/2), 2) * Math.cos(Math.toRadians(fromLat)) * Math.cos(Math.toRadians(toLat))));

    }

    public Double getZoneDistance(Double fromLat,Double fromLng,Double toLat, Double toLng) {
        Double theR = 6378.137;
        Double ad = Math.sin(fromLat/2)*Math.cos(toLat/2)+ Math.cos(fromLat*Math.PI/180)*Math.cos(toLat*Math.PI/180)* Math.sin(fromLng/2)*Math.sin(toLng/2);
        Double cd = 2*Math.atan2(Math.sqrt(ad),Math.sqrt(1-ad));
        Double dc = theR*cd;

        return dc*1000;
    }

    public int degreesToMicrodegrees(double degrees) {
        return (int) (degrees * MICRODEGREES_COEFF);
    }

    public double microdegreesToDegrees(int microdegrees) {
        return (double) microdegrees / (double) MICRODEGREES_COEFF;
    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public String CDate(String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date=new Date();
        String cdate=sdf.format(date);
        return cdate;
    }

    public String filterDate(String datestr, String inDateFormat, String outDateFormat) {
        try{
            SimpleDateFormat sdfb = new SimpleDateFormat(inDateFormat);
            SimpleDateFormat sdfa = new SimpleDateFormat(outDateFormat);
            return sdfa.format(sdfb.parse(datestr));
        }catch(Exception ex){
            return "";
        }
    }
}
