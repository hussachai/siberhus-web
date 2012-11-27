package com.siberhus.web.servlet.filter.performance;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasurementFilter implements Filter {
	
	private final Logger L = LoggerFactory.getLogger(MeasurementFilter.class);
	
	public String varName = "measurement";
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		String varNameParam = config.getInitParameter("VarName");
		if(varNameParam!=null){
			varName = varNameParam;
		}
		L.info("Performance->MeasurementFilter variable name: {}",varName);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		L.debug("Start filter");
		Measurement m = new Measurement();
		m.init();
		request.setAttribute(varName, m);
		
		chain.doFilter(request, response);
		
		L.debug("Measurement: {}",m);
		L.debug("End filter");
	}
	
	@Override
	public void destroy() {

	}
	
}
