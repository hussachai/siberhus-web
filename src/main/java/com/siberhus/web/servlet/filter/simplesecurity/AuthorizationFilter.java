package com.siberhus.web.servlet.filter.simplesecurity;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public abstract class AuthorizationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(
				(HttpServletRequest) request) {
			@Override
			public String getAuthType() {
				return HttpServletRequest.FORM_AUTH;
			}

			@Override
			public boolean isUserInRole(String role) {
				return _isUserInRole(this, role);
			}
			
			@Override
			public Principal getUserPrincipal() {
				return _getUserPrincipal(this);
			}

		};

		chain.doFilter(requestWrapper, response);

	}

	@Override
	public void destroy() {
	}

	protected abstract boolean isUserInRole(HttpServletRequest request,
			String role);

	protected abstract String getUsername(HttpServletRequest request);
	
	private Principal _getUserPrincipal(final HttpServletRequest request) {
		return new Principal() {
			@Override
			public String getName() {
				return getUsername(request);
			}
		};
	}

	private boolean _isUserInRole(HttpServletRequest request, String role) {
		return this.isUserInRole(request, role);
	}
	
}
