
import android.content.Context;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


/**
 * Created by makis on 1/10/2017.
 */

public class DatesFunctions {

    private Context mContext;

    public DatesFunctions(Context context){
        this.mContext = context;
    }

    /**
     * Function to get the number of weeks a month has
     * @param month which month we want to check (as integer)
     * @param year of which year
     * @return
     */
    public Integer[] getWeeksOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Set<Integer> weeks = new HashSet<Integer>();
        int ndays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < ndays; i++) {
            weeks.add(cal.get(Calendar.WEEK_OF_YEAR));
            cal.add(Calendar.DATE, 1);
        }
        return weeks.toArray(new Integer[0]);
    }

    /**
     * Compares if Date1 is after Date2 for a date format we want
     * @param date1 First date to see if after date2
     * @param date2 Second date
     * @param format Format that the two above dates are (both dates must be of same format)
     * @return Returns true if date1 is after date2, false otherwise
     */
    public boolean compareDate1AfterDate2(String date1, String date2, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date convertedDate1 = null;
        Date convertedDate2 = null;
        try {
            convertedDate1 = dateFormat.parse(date1);
            convertedDate2 = dateFormat.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(convertedDate1!=null && convertedDate2!=null){
            if(convertedDate1.after(convertedDate2)){
                return true;
            }
        }
        return false;
    }

    /**
     * Function to calculate the difference between two dates of a given format
     * @param date_one First date passses
     * @param date_two Second date passed
     * @param dateFormat The format that the two dates are (both dates must be of the same format)
     * @return Returns the days that the two dates differ
     */
    public int getDaysDifferenceTwoDates(String date_one, String date_two, String dateFormat) {
        int diffint = 0;
        try{
            SimpleDateFormat date_created = new SimpleDateFormat(dateFormat);
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

            Date date1 = date_created.parse(date_one);
            Date date2 = formatter.parse(date_two);

            long diff2 = Math.abs(date1.getTime() - date2.getTime());
            long diffDays = diff2 / (24 * 60 * 60 * 1000);
            diffint = (int) diffDays;
            diffint = diffint+1;
        }catch (ParseException e1){
            e1.printStackTrace();
        }
        return diffint;
    }

    /**
     * Function to add a specific number of days to a given date of a given format
     * @param date Date we want to add the days
     * @param format Format of the given date
     * @param days Number of days we want to add
     * @return Returns the new calculated date in the passed format
     */
    public String addDaysToDate(String date, String format, int days){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, days);
        date = sdf.format(c.getTime());
        return date;
    }

    /**
     * Function that filters a given date of a given format to a new given format
     * @param datestr The date we want to format
     * @param inDateFormat The current date format
     * @param outDateFormat The new format we want
     * @return Returns the given date in the new format if everything went ok, "" otherwise
     */
    public String filterDate(String datestr, String inDateFormat, String outDateFormat) {
        try{
            SimpleDateFormat sdfb = new SimpleDateFormat(inDateFormat);
            SimpleDateFormat sdfa = new SimpleDateFormat(outDateFormat);
            return sdfa.format(sdfb.parse(datestr));
        }catch(Exception ex){
            return "";
        }
    }

    /**
     * Function to get now date in given format
     * @param dateFormat The format we want our date output
     * @return Returns now date in given format
     */
    public String CDate(String dateFormat){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date=new Date();
            String cdate=sdf.format(date);
            return cdate;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Function to get months name based on month int
     * @param num
     * @return
     */
    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    /**
     * Function to calculate the start day and end day of a given week in a given format
     * @param enterWeek Week integer
     * @param enterYear Year of date
     * @param dateFormat Date format
     * @param splitSymbol Symbol to split the two values
     * @return
     */
    public String getStartEndOFWeek(int enterWeek, int enterYear, String dateFormat, String splitSymbol){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.set(Calendar.WEEK_OF_YEAR, enterWeek);
        calendar.set(Calendar.YEAR, enterYear);

        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);

        DebugLogger.debug("Current week started at - "+startDateInStr+" - ends in - "+endDaString);
        return startDateInStr+""+splitSymbol+""+endDaString;
    }

    /**
     * Function to calculate the week number of a given date
     * @param inputDate given date
     * @param dateFormat date format
     * @return Returns the week number of the given date
     */
    public int calculateWeekNum(String inputDate, String dateFormat) {
        String input = inputDate;
        String format = dateFormat;

        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        DebugLogger.debug("Weeks number is "+week);
        return week;
    }

    public String getDateFromCalendar(Calendar calendar, String frm){
        SimpleDateFormat format = new SimpleDateFormat(frm);
        return format.format(calendar.getTime());
    }

    public Calendar getCalendarFromDate(String date, String format){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        try {
            cal.setTime(sdf.parse(date));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal;
    }
}
