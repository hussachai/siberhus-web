package com.siberhus.web.ui.js.format;

import java.util.ArrayList;
import java.util.List;

public interface DateFormatTranslator {

	public static final List<Character> JAVA_DFMT_ALL_CHARS = new ArrayList<Character>(){
		private static final long serialVersionUID = 1L;

		{
			char chars[] = new char[]{'G','y','M','w','W','D','d','F','E','a','H','k','K','h','m','s','S','z','Z'};
			for(char c: chars){
				add(c);
			}
		}
	};
	
	public String translate(String pattern);
	
	public String translate(String pattern, boolean validate);
	
}
