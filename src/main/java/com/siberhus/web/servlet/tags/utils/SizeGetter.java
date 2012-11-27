/*
 * SizeGetter.java
 *
 * Created on 21 a�Ҥ� 2550, 16:13 �.
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.siberhus.web.servlet.tags.utils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 * @author Hussachai
 */
public class SizeGetter extends TagSupport{
    
    private JspWriter out;
    private Object target;
    
    /**
     * Creates a new instance of SizeGetter
     */
    public SizeGetter() {
    }
    

    public int doStartTag() throws JspException {
        setOut(pageContext.getOut());
        try{
            if(getTarget()!=null){
                if(getTarget() instanceof String){
                    getOut().print( ((String)getTarget()).length() );
                }else if(getTarget() instanceof Collection){
                    getOut().print( ((Collection)getTarget()).size() );
                }else if(getTarget() instanceof Map){
                    getOut().print( ((Map)getTarget()).size() );
                }else if(getTarget().getClass().isArray()){
                    getOut().print(Array.getLength(getTarget()));
                }else{
                    getOut().print("");
                }
            }else{
                getOut().print("");
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public JspWriter getOut() {
        return out;
    }

    public void setOut(JspWriter out) {
        this.out = out;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
    
    public void release(){
        target = null;
    }
}
