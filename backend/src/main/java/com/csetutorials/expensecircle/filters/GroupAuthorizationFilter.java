package com.csetutorials.expensecircle.filters;

import com.csetutorials.expensecircle.beans.UserInfo;
import com.csetutorials.expensecircle.entities.GroupUser;
import com.csetutorials.expensecircle.services.GroupUserService;
import com.csetutorials.expensecircle.services.LoggedInUserInfoService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class GroupAuthorizationFilter implements Filter {

	@Autowired
	private LoggedInUserInfoService loggedInUserInfoService;
	@Autowired
	private GroupUserService groupUserService;

	@Override
	public void init(FilterConfig filterConfig) {
		// Initialization logic if needed
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String path = httpRequest.getRequestURI();

		if (path.matches("/api/groups/\\d+/.*")) {
			String[] paths = path.split("/");
			String groupId = paths[3]; // Extract groupId from the path
			UserInfo userInfo = loggedInUserInfoService.getInfo();

			Optional<GroupUser> opt = groupUserService.findByGroupIdAndUserId(Long.parseLong(groupId), userInfo.getEmail());
			if (opt.isEmpty()) {
				HttpServletResponse httpServletResponse = (HttpServletResponse) response;
				httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to access this resource");
				return;
			}
		}

		// Continue with the filter chain
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// Cleanup logic if needed
	}
}
