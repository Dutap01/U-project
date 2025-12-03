package U_project;

import java.util.Date;
import java.text.SimpleDateFormat;

public interface time {
    Date d = new Date();
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
    SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
    SimpleDateFormat minFormat = new SimpleDateFormat("mm");
    
    String year = yearFormat.format(d);
    String month = monthFormat.format(d);
    String day = dayFormat.format(d);
    String hour = hourFormat.format(d);
    String min = minFormat.format(d);
}