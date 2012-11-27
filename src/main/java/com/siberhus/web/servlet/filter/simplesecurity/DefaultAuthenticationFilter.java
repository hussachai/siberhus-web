package com.siberhus.web.servlet.filter.simplesecurity;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;


public class DefaultAuthenticationFilter extends AuthenticationFilter{
	
	public static final String PARAM_PUBLIC_URLS = "PublicUrls";
	
	public static final String PARAM_UNAUTHEN_URL = "UnauthenticatedUrl";
	
	public static final String PARAM_REDIRECT_PARAM_NAME = "RedirectParamName";
	
	public static final String PARAM_URL_ENCODING = "UrlEncoding";
	
	public static final String PARAM_USER_SESSION = "UserSession";
	
	private Set<String> publicUrls = new HashSet<String>();
	
	private String unauthenticatedUrl;
	
	private String urlEncoding;
	
	private String redirectParamName;
	
	private String userSession;
	
	public void init(FilterConfig config) throws ServletException {
		
//		publicUrls.add("/page/login.htm");
//		publicUrls.add("/page/index.jsp");
		
		String publicUrlsParam = config.getInitParameter(PARAM_PUBLIC_URLS);
		
		if(publicUrlsParam!=null){
			
			String[] urls = StringUtils.split(publicUrlsParam,",");
			for(String url : urls){
				url = StringUtils.trimToNull(url);
				if(url!=null){
					publicUrls.add(url);
				}
			}
			
		}else{
			
			throw new ServletException(PARAM_PUBLIC_URLS+" filter init-param is missing");
		}
		
		unauthenticatedUrl = config.getInitParameter(PARAM_UNAUTHEN_URL);
		unauthenticatedUrl = StringUtils.trimToNull(unauthenticatedUrl);
		
		if(unauthenticatedUrl==null){
			throw new ServletException(PARAM_UNAUTHEN_URL+" filter init-param is missing");
		}else{
			publicUrls.add(unauthenticatedUrl);
		}
		
		urlEncoding = config.getInitParameter(PARAM_URL_ENCODING);
		
		redirectParamName = config.getInitParameter(PARAM_REDIRECT_PARAM_NAME);
		
		userSession = config.getInitParameter(PARAM_USER_SESSION);
		
	}
	
	@Override
	protected Set<String> getPublicUrls() {
		
		return publicUrls;
	}

	@Override
	protected String getUnauthenticatedUrl() {
		
		return unauthenticatedUrl;
	}

	@Override
	protected String getUrlEncoding() {
		
		if(urlEncoding!=null){
			return urlEncoding;
		}else{
			return super.getUrlEncoding();
		}
	}

	@Override
	protected String getRedirectParamName() {
		
		if(redirectParamName!=null){
			return redirectParamName;
		}else{
			return super.getRedirectParamName();
		}
		
	}

	@Override
	protected String getUserSession() {
		
		if(userSession!=null){
			return userSession;
		}else{
			return super.getUserSession();
		}
	}
	
	

	
}
