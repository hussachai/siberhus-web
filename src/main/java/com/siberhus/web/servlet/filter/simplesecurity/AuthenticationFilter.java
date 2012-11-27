package com.siberhus.web.servlet.filter.simplesecurity;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;

public abstract class AuthenticationFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;

		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (request.getSession().getAttribute(getUserSession()) != null) {

			chain.doFilter(request, response);

		} else if (isPublicResource(request)) {

			chain.doFilter(request, response);

		} else {

			StringBuilder redirectURL = new StringBuilder();
			String targetURL = null;
			String targetQueryString = null;

			redirectURL.append(request.getContextPath()).append(
					getUnauthenticatedUrl());

			if (getRedirectParamName() != null) {

				redirectURL.append("?").append(getRedirectParamName()).append(
						"=");
				if (!"/".equals(request.getServletPath())) {
					targetURL = URLEncoder.encode(request.getServletPath(),
							getUrlEncoding());
					redirectURL.append(targetURL);
				}
				targetQueryString = request.getQueryString();

				if (targetQueryString != null) {
					targetQueryString = URLEncoder.encode("?"
							+ targetQueryString, getUrlEncoding());
					redirectURL.append(targetQueryString);
				}
			}

			response.sendRedirect(redirectURL.toString());
		}
	}

	@Override
	public void destroy() {
	}

	protected abstract Set<String> getPublicUrls();

	protected abstract String getUnauthenticatedUrl();

	protected String getRedirectParamName() {
		return "redirect";
	}

	protected String getUrlEncoding() {
		return "UTF-8";
	}

	protected String getUserSession() {
		return "user";
	}
	
	private boolean isPublicResource(HttpServletRequest request) {
		String resource = request.getServletPath();
		if (getPublicUrls().contains(resource)) {
			return true;
		}
		return false;
	}

}
