
package com.siberhus.web.servlet.tags.pager;

/*
 * PageList.java
 *
 * Created on 18 a�Ҥ� 2550, 17:02 �.
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.*;
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
public class ListPager extends TagSupport{

	private static final long serialVersionUID = 1L;

	private JspWriter out;
    
    //Require
    private Object items; /* object name or real object of List from any scope */
    private String var; /* outcome object to request scope */
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
    public ListPager() {
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
    
    public int doStartTag()throws JspException{
        ///////////////////////////////////////
        Enumeration<?> attribNames = pageContext.getRequest().getAttributeNames();
        //System.out.println("##############################");
        while(attribNames.hasMoreElements()){
            //System.out.print("###>");
            //Object attribName = attribNames.nextElement();
            //System.out.println(attribName);
            //Object objBean = pageContext.getRequest().getAttribute(attribName.toString());
            //System.out.println(objBean);
        }
        //////////////////////////////////////////////////
        
        List list = null;
        setOut(pageContext.getOut());
        if(getOffset()>=0){
            if( (getOffset()+getLimit()) <= getInputList().size() ){
                list = getInputList().subList(getOffset(),getOffset()+getLimit());
            }else{
                if(getOffset()<getInputList().size()){
                    list = getInputList().subList(getOffset(),getInputList().size());
                }
            }
        }
        pageContext.setAttribute(getVar(),list,PageContext.REQUEST_SCOPE);
        //System.out.println("Bean : "+list);
        
        String pageNavHtmlCode = getDropdownNavCode();
        if(getPageNavStyle().equalsIgnoreCase("dropdown") ||
                getPageNavStyle().equalsIgnoreCase("combobox")){
            pageContext.setAttribute(getPageNavVar(),pageNavHtmlCode,PageContext.REQUEST_SCOPE);
        }else{
            throw new JspException("UnknowStyleException");
        }
        
        return EVAL_BODY_INCLUDE;
    }
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
        for(int pageOffset=0; pageOffset < getInputList().size();pageOffset+=getLimit(),pageNumber++){
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
    
    public Object getItems() {
        return items;
    }
    
    public void setItems(Object items) {
        this.items = items;
    }
    
    public String getVar() {
        return var;
    }
    
    public void setVar(String var) {
        this.var = var;
    }
    
    
    /* not accessible.for internal use only */
    private List getInputList() {
        Object o = null;
        if(getItems()==null){
            //System.out.println("Items = NULL");
            return new ArrayList(0);
        }
        if(getItems() instanceof String){
            //System.out.println("Items = String");
            /* Find it in any scope */
            o = pageContext.findAttribute(getItems().toString());
        }else{
            //System.out.println("Items = RealObject");
            o = getItems();
            //System.out.println(o.getClass().getName());
        }
        return castObjectToList(o);
    }
    
    /* not accessible.for internal use only */
//    private List getOutputList() {
//        Object o = pageContext.getAttribute(getVar(),pageContext.REQUEST_SCOPE);
//        return castObjectToList(o);
//    }
    
    private List castObjectToList(Object o){
        if(o!=null && (o instanceof List)){
            return (List)o;
        }else{
            if(o==null){
                return new ArrayList(0);
            }else{
                throw new ClassCastException();
            }
        }
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
        items = null; /* object name or real object of List from any scope */
        var = null; /* outcome object to request scope */
        offset = 0;
        limit = 0;
        
        //Optional
        pageNavVar = "pageNav";
        paramOffsetName = "pageOffset"; /* param name is used in <select name="paramName"> */
        uri = null; /* default is request URI */
        pageNavStyle = "dropdown";
        anchorVar = null;
        pageNumVar = null;
    }
    
}

