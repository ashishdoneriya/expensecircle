package com.csetutorials.expensecircle.projection;

import com.csetutorials.expensecircle.beans.Role;

public interface GroupUserProjection {

	String getUserId();

	String getUserName();

	Long getGroupId();

	String getGroupName();

	Role getRole();

}
