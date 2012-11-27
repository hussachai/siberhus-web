package com.siberhus.web.servlet.filter.performance;


public class Measurement {
	
	private long elapsedTime;
	
	private long maxMemory;
	
	private long freeMemory;
	
	private long usedMemory;
	
	private String displayValue;
	
	public void init(){
		elapsedTime = System.currentTimeMillis();
		maxMemory = Runtime.getRuntime().maxMemory();
		freeMemory = Runtime.getRuntime().freeMemory();
	}
	
	@Override
	public String toString(){
		if(displayValue==null){
			calculate();
		}
		displayValue = "{elapsedTime: "+elapsedTime+" ms, maxMemory: "+maxMemory
		+" bytes, freeMemory: "+freeMemory+" bytes, usedMemory: "+usedMemory+" bytes}";
		return displayValue;
	}
	
	public void calculate(){
		elapsedTime = (System.currentTimeMillis()-elapsedTime);
		usedMemory = freeMemory-Runtime.getRuntime().freeMemory();
		if(usedMemory<0){
			usedMemory = -1;
		}
		freeMemory = Runtime.getRuntime().freeMemory();
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}
	
	
}
