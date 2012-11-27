package com.siberhus.web.ui.js.format;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public enum JQueryDateFormatTranslator implements DateFormatTranslator{
	
	INSTANCE;
	
	public static final List<Character> JAVA2JQUERY_DF_CHARS = new ArrayList<Character>(){
		private static final long serialVersionUID = 1L;
		{
			char chars[] = new char[]{'y','M','d','E'};
			for(char c: chars){
				add(c);
			}
		}
	};
	
	public String translate(SimpleDateFormat dateFmt, boolean validate){
		return translate(dateFmt.toPattern(), validate);
	}
	
	public String translate(String pattern){
		return translate(pattern, false);
	}
	
	/**
	 * Does not support literal in quotes
	 */
	public String translate(String pattern, boolean validate){
		StringBuilder result = new StringBuilder();
		List<String> tokens = new ArrayList<String>();
		String buf = "";
		char prec = 0;
		for(char c : pattern.toCharArray()){
			if(prec==0) prec = c;
			if(prec==c){
				buf += c;
			}else{
				tokens.add(buf);
				buf = String.valueOf(c);
			}
			prec = c;
		}
		tokens.add(buf);
		for(String token: tokens){
			char c = token.charAt(0);
			int l = token.length();
			if(c=='y'){
				//y - year (two digit)
				//yy - year (four digit)
				if(l<=3){
					result.append('y');
				}else{
					for(int i=0;i<l-4;i++){
						result.append('0');
					}
					result.append("yy");
				}
			}else if(c=='M'){
				//m - month of year (no leading zero)
				//mm - month of year (two digit)
				//M - month name short
				//MM - month name long
				if(l==1){
					result.append('m');
				}else if(l==2){
					result.append("mm");
				}else if(l==3){
					result.append('M');
				}else{
					result.append("MM");
				}
			}else if(c=='d'){
				//d - day of month (no leading zero)
				//dd - day of month (two digit)
				if(l==1){
					result.append('d');
				}else{
					for(int i=0;i<l-2;i++){
						result.append('0');
					}
					result.append("dd");
				}
			}else if(c=='E'){
				//M - month name short
				//MM - month name long
				if(l<4){
					result.append('M');
				}else{
					result.append("MM");
				}
			}else if(c=='\''){
				if(l%2!=0){
					throw new IllegalArgumentException("Unterminated quote");
				}
				result.append(token);
			}else{
				result.append(token);
			}
		}
		return result.toString();
	}
	
	public static void main(String[] args) {
		String s = JQueryDateFormatTranslator.INSTANCE.translate("dd yyyy");
		System.out.println(s);
	}
}
