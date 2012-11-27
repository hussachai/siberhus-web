/*
 * ListPager.java
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.siberhus.web.servlet.tags.pager;

/*
 * PageList.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 * @author Hussachai
 */
@Deprecated
public class BasicPager extends TagSupport{
	
	private static final long serialVersionUID = 1L;
	
	private JspWriter out;
    //Require
    private int rownum;
    private int offset;
    private int limit;
    
    //Optional
    private String pageNavVar = "pageNav";
    private String paramOffsetName = "pageOffset"; /* param name is used in <select name="paramName"> */
    private String uri; /* default is request URI */
    private String pageNavStyle = "dropdown";
    private String anchorVar;
    private String pageNumVar;
    
    /** Creates a new instance of PageList */
    public BasicPager() {
    }
    
    public String getUri() {
        if(uri == null){
            /* If uri attribute is not set */
            uri = ((HttpServletRequest)pageContext.getRequest()).getRequestURI();
        }
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    @Override
    public int doStartTag()throws JspException{

        String pageNavHtmlCode = getDropdownNavCode();
        if(getPageNavStyle().equalsIgnoreCase("dropdown") ||
                getPageNavStyle().equalsIgnoreCase("combobox")){
            pageContext.setAttribute(getPageNavVar(),pageNavHtmlCode,PageContext.REQUEST_SCOPE);
        }else{
            throw new JspException("UnknowStyleException");
        }
        
        return SKIP_BODY;
    }
    @Override
    public int doEndTag()throws JspException{
        //
        return EVAL_PAGE;
    }
    /*
    private String getDropdownNavCode(){
        StringBuffer pageNavHtmlCode = new StringBuffer();
        pageNavHtmlCode.append("<form name='ASDFGHJKLQWERTYU' action='");
        if(getUri()==null){
            pageNavHtmlCode.append(getRequestURI()+"' method='"+getRequestMethod()+"'>");
        }else{
            pageNavHtmlCode.append(getUri()+"' method='get'>");
        }
        pageNavHtmlCode.append("<select name='"+getParamName()+"' onChange='document.ASDFGHJKLQWERTYU.submit()'>");
        for(int pageOffset=0,pageNumber=1; pageOffset < getInputList().size();pageOffset+=getLimit(),pageNumber++){
            //System.out.println("PageOffSet = "+i);
            if(getOffset()==pageOffset){
                //System.out.println("PageOff = i");
                pageNavHtmlCode.append("<option value='"+pageOffset+"' selected>"+pageNumber+"</value>");
            }else{
                pageNavHtmlCode.append("<option value='"+pageOffset+"'>"+pageNumber+"</value>");
            }
        }
        pageNavHtmlCode.append("</select>");
        pageNavHtmlCode.append("</form>");
        return pageNavHtmlCode.toString();
    }
     */
    private String getDropdownNavCode(){
        int pageNumber = 1;
        StringBuffer pageNavHtmlCode = new StringBuffer();
        pageNavHtmlCode.append("<select id='");
        pageNavHtmlCode.append(getParamOffsetName());
        pageNavHtmlCode.append("' onChange='");
        pageNavHtmlCode.append("window.location=\"");
        pageNavHtmlCode.append(getUri());
        if(pageContext.getRequest().getParameterNames().hasMoreElements()){
           pageNavHtmlCode.append("&");
        }else{
           pageNavHtmlCode.append("?"); 
        }
        pageNavHtmlCode.append(getParamOffsetName());
        pageNavHtmlCode.append("=\"+document.getElementById(\"");
        pageNavHtmlCode.append(getParamOffsetName());
        pageNavHtmlCode.append("\").value");
        pageNavHtmlCode.append("+\"");
        pageNavHtmlCode.append(getAnchorVar());
        pageNavHtmlCode.append("\"");
        pageNavHtmlCode.append("'>");
        for(int pageOffset=0; pageOffset < getRownum();pageOffset+=getLimit(),pageNumber++){
            //System.out.println("PageOffSet = "+i);
            if(getOffset()==pageOffset){
                //System.out.println("PageOff = i");
                pageNavHtmlCode.append("<option value='"+pageOffset+"' selected>"+pageNumber+"</value>");
            }else{
                pageNavHtmlCode.append("<option value='"+pageOffset+"'>"+pageNumber+"</value>");
            }
        }
        if(getPageNumVar()!=null){
            pageContext.setAttribute(getPageNumVar(),Integer.toString(--pageNumber),PageContext.REQUEST_SCOPE);
        }
        pageNavHtmlCode.append("</select>");
        return pageNavHtmlCode.toString();
    }
    public JspWriter getOut() {
        return out;
    }
    
    public void setOut(JspWriter out) {
        this.out = out;
    }
    
    public int getOffset() {
        return offset;
    }
    
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public int getLimit() {
        return limit;
    }
    
    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public String getParamOffsetName() {
        return paramOffsetName;
    }
    
    public void setParamOffsetName(String paramOffsetName) {
        this.paramOffsetName = paramOffsetName;
    }
    
    public String getPageNavVar() {
        return pageNavVar;
    }
    
    public void setPageNavVar(String pageNavVar) {
        this.pageNavVar = pageNavVar;
    }
    
    public String getPageNavStyle() {
        return pageNavStyle;
    }
    
    public void setPageNavStyle(String pageNavStyle) {
        this.pageNavStyle = pageNavStyle;
    }
    
    public String getAnchorVar() {
        if(anchorVar!=null){
            return "#"+anchorVar;
        }else{
            return "";
        }
    }
    
    public void setAnchorVar(String anchorVar) {
        this.anchorVar = anchorVar;
    }
    
    public String getPageNumVar() {
        return pageNumVar;
    }
    
    public void setPageNumVar(String pageNumVar) {
        this.pageNumVar = pageNumVar;
    }
    
    public void release(){
        setOffset(0);
        setLimit(0);
        
        //Optional
        setPageNavVar("pageNav");
        setParamOffsetName("pageOffset"); /* param name is used in <select name="paramName"> */
        setUri(null); /* default is request URI */
        setPageNavStyle("dropdown");
        setAnchorVar(null);
        setPageNumVar(null);
    }

    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }
    
}

