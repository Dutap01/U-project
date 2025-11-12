import java.util.*;

public interface time
{
	Calendar present = Calendar.getInstance();
	int day = present.get(Calendar.DATE);
	int hour = present.get(Calendar.HOUR_OF_DAY);
	int min = present.get(Calendar.MINUTE);
	int month = present.get(Calendar.MONTH) + 1;
	int sec = present.get(Calendar.SECOND);
	int year = present.get(Calendar.YEAR);
}