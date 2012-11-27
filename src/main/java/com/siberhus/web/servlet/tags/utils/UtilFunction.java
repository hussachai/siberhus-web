/*
 * UtilFunction.java
 *
 * Created on 3 �չҤ� 2550, 21:57 �.
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.siberhus.web.servlet.tags.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Hussachai
 */
@Deprecated
public class UtilFunction {

    private UtilFunction() {
    }
    
    public static int getSize(Object obj){
        if(obj==null){
            return 0;
        }else if(obj instanceof String){
            return ((String)obj).length();
        }else if(obj instanceof Collection){
            return ((Collection)obj).size();
        }else if(obj instanceof Map){
            return ((Map)obj).size();
        }else if(obj.getClass().isArray()){
            return Array.getLength(obj);
        }else{
            return 0;
        }
    }
    
    public static boolean isEmpty(Object obj){
        if(obj==null){
            return true;
        }else if(obj instanceof String){
            return (((String)obj).length()==0);
        }else if(obj instanceof Collection){
            return (((Collection)obj).size()==0);
        }else if(obj instanceof Map){
            return (((Map)obj).size()==0);
        }else if(obj.getClass().isArray()){
            return (Array.getLength(obj)==0);
        }else{
            return true;
        }
    }
    
}
