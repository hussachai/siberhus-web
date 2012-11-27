package com.siberhus.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public final class CookieUtils {
	
	/**
	 * Get the first cookie of specified name
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie get(HttpServletRequest request, String name){
		if(request.getCookies()==null){
			return null;
		}
		for(Cookie cookie : request.getCookies()){
			if(cookie.getName().equals(name)){
				return cookie;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param request
	 * @param name
	 * @param domain
	 * @param path
	 * @return
	 */
	public static Cookie get(HttpServletRequest request, String name, String path, String domain){
		if(request.getCookies()==null){
			return null;
		}
		for(Cookie cookie : request.getCookies()){
			if(cookie.getName().equals(name)){
				if(path!=null){
					if(!StringUtils.equals(cookie.getPath(),path)){
						continue;
					}
				}
				if(domain!=null){
					if(!StringUtils.equals(cookie.getDomain(),domain)){
						continue;
					}
				}
				return cookie;
			}
		}
		return null;
	}
	
	/**
	 * Delete the first cookie of specified name
	 * @param request
	 * @param name
	 * @return true if cookie found, false otherwise
	 */
	public static Cookie delete(HttpServletRequest request, String name){
		Cookie cookie = get(request, name);
		if(cookie!=null){
			cookie.setMaxAge(0);
			cookie.setValue(null);
			return cookie;
		}
		return null;
	}
	
	public static Cookie delete(HttpServletRequest request, 
			String name, String path, String domain){
		Cookie cookie = get(request, name, path, domain);
		if(cookie!=null){
			cookie.setMaxAge(0);
			cookie.setValue(null);
			return cookie;
		}
		return null;
	}
	
}
