package com.csetutorials.expensecircle.projection;

import com.csetutorials.expensecircle.beans.Role;

public interface GroupUserProjection {

	String getUserId();

	String getUserName();

	String getGroupId();

	String getGroupName();

	Role getRole();

}
