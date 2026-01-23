package com.csetutorials.expensecircle.utilities;

import com.csetutorials.expensecircle.beans.DayOfWeek;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

public class DateUtils {

	public static final Map<DayOfWeek, java.time.DayOfWeek> WEEK_MAP = Map.of(
		DayOfWeek.SUN, java.time.DayOfWeek.SUNDAY,
		DayOfWeek.MON, java.time.DayOfWeek.MONDAY,
		DayOfWeek.TUE, java.time.DayOfWeek.TUESDAY,
		DayOfWeek.WED, java.time.DayOfWeek.WEDNESDAY,
		DayOfWeek.THU, java.time.DayOfWeek.THURSDAY,
		DayOfWeek.FRI, java.time.DayOfWeek.FRIDAY,
		DayOfWeek.SAT, java.time.DayOfWeek.SATURDAY
	);

	public static ZonedDateTime getDateTime(long timestamp, String timezone) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		ZoneId zone = ZoneId.of(timezone);
		return ZonedDateTime.ofInstant(instant, zone);
	}

}
