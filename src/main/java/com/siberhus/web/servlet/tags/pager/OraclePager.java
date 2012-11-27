package com.siberhus.web.servlet.tags.pager;


/**
 *
 * @author Hussachai
 */
@Deprecated
public class OraclePager {
	
	
	private static String DEFAULT_ROWNUM_NAME = "default_rownum";
	
    /** Creates a new instance of OraclePager */
    public OraclePager() {
    }
    
    public static StringBuffer getSQLPager(StringBuffer sql,int begin,int end,String rownumName){
    	if(rownumName==null)rownumName = DEFAULT_ROWNUM_NAME;
        sql.insert(0," , a.* from(");
        sql.insert(0,rownumName);
        sql.insert(0,"select b.* from (select rownum ");
        sql.append(") a where rownum <= ");
        sql.append(end);
        sql.append(" ) b where ");
        sql.append(rownumName);
        sql.append(" >=");
        sql.append(begin);
        return sql;
    }
    public static StringBuffer getSQLPager(StringBuffer sql,int begin,int end){
    	return getSQLPager(sql,begin,end,null);
    }
    public static String getSQLPager(String sql,int begin,int end,String rownumName){
    	if(rownumName==null)rownumName = DEFAULT_ROWNUM_NAME;
        return "select b.* from (select rownum "+rownumName+" , a.* from("+sql+
        ") a where rownum <= "+end+" ) b where "+rownumName+" >="+begin;
    }
    public static String getSQLPager(String sql,int begin,int end){
    	return getSQLPager(sql,begin,end,null);
    }
    
    public static void main(String args[]){
        System.out.println(getSQLPager("select * from users",2,3,"myRowNum"));
        System.out.println(getSQLPager(new StringBuffer("select * from users"),2,3,"myRowNum"));
    }
}
