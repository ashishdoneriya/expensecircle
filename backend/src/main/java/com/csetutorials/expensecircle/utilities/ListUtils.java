package com.csetutorials.expensecircle.utilities;

import java.util.Collection;

public final class ListUtils {

	public static <T> boolean isEmpty(Collection<T> list ) {
		return list == null || list.isEmpty();
	}

}
