package hij.cache.extension;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hij.util.generic.IFuncP1;

/**
 * 
 * SQL参数对象及其处理辅助类
 *    参考自:http://blog.csdn.net/wallimn/article/details/3734242
 * @author XuminRong
 *
 */
public final class SQLParams {
	String sql;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Map<Integer, String> getParams() {
		return params;
	}
	public void setParams(Map<Integer, String> params) {
		this.params = params;
	}
	Map<Integer, String> params = new HashMap<Integer, String>();

    /**
     * 分析处理带命名参数的SQL语句。使用Map存储参数，然后将参数替换成?
     * @param sql
     * @return
     */
    public static SQLParams parse(String sql) {
    	SQLParams param = new SQLParams();
        String regex = "(@(\\w+))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sql);
        int idx=1;
        while (m.find()) {
            //参数名称可能有重复，使用序号来做Key
        	param.getParams().put(new Integer(idx++), m.group(2));
            //System.out.println(m.group(2));
        }
        String result = sql.replaceAll(regex, "?");
        param.setSql(result);
        return param;
    }
    /**
     * 使用参数值Map，填充pStat
     * @param pStat
     * @param pMap 命名参数的值表，其中的值可以比较所需的参数多。
     * @return
     */
    public static boolean fillParameters(PreparedStatement pStat, SQLParams param, IFuncP1<String,Object> func){
    	if (pStat == null || param == null) {
    		return false;
    	}
    	if (param.getParams().size() > 0 && func == null) {
    		return false;
    	}
    	for (Integer key : param.getParams().keySet()) {  
    		String paramName = param.getParams().get(key);
    		Object val = func.handle(paramName);
    		try
    		{
        		pStat.setObject(key, val);          			
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
    			return false;
    		}
    	}
        return true;
    }
}
