
package com.siberhus.web.servlet.tags.pager;

import java.io.Serializable;


/**
 *
 * @author Hussachai
 */
@Deprecated
public class OraclePagerBean implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rownumName;
    private int begin;
    private int end;
    
    /** Creates a new instance of PagerBean */
    public OraclePagerBean(){}
    
    public OraclePagerBean(String rownumName,int begin,int end) {
        this.rownumName = rownumName;
        this.begin = begin;
        this.end= end;
    }
    
    public OraclePagerBean(String rownumName,String begin,String end) {
        this.rownumName = rownumName;
        this.begin = Integer.parseInt(begin);
        this.end= Integer.parseInt(end);
    }
    
    public String getRownumName() {
        return rownumName;
    }

    public void setRownumName(String rownumName) {
        this.rownumName = rownumName;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }
    
    public void setBegin(String begin){
        this.begin = Integer.parseInt(begin);
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
    
    public void setEnd(String end){
        this.end = Integer.parseInt(end);
    }
}
