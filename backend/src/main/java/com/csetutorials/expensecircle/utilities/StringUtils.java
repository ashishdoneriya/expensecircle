package com.csetutorials.expensecircle.utilities;

public class StringUtils {

	public static boolean isNotBlank(String str) {
		return str != null && !str.trim().isEmpty();
	}

}
