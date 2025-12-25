package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class LoggedInUserInfoService {

	private final InheritableThreadLocal<UserInfo> loggedInUserInfo = new InheritableThreadLocal<>();

	public UserInfo getInfo() {
		return loggedInUserInfo.get();
	}

	public String getId() {
		return loggedInUserInfo.get().getUserId();
	}

	public void setInfo(UserInfo info) {
		loggedInUserInfo.set(info);
	}

}
