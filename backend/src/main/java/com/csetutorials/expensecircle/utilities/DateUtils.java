package com.csetutorials.expensecircle.utilities;

import com.csetutorials.expensecircle.beans.DayOfWeek;

import java.util.Calendar;
import java.util.Map;

public class DateUtils {

	public static final Map<DayOfWeek, Integer> WEEK_MAP = Map.of(
		DayOfWeek.SUN, Calendar.SUNDAY,
		DayOfWeek.MON, Calendar.MONDAY,
		DayOfWeek.TUE, Calendar.TUESDAY,
		DayOfWeek.WED, Calendar.WEDNESDAY,
		DayOfWeek.THU, Calendar.THURSDAY,
		DayOfWeek.FRI, Calendar.FRIDAY,
		DayOfWeek.SAT, Calendar.SATURDAY
	);

}
