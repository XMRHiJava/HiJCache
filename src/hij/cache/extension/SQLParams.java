package hij.cache.extension;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hij.util.generic.IFuncP1;

/**
 * 
 * SQL���������䴦������
 *    �ο���:http://blog.csdn.net/wallimn/article/details/3734242
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
     * �������������������SQL��䡣ʹ��Map�洢������Ȼ�󽫲����滻��?
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
            //�������ƿ������ظ���ʹ���������Key
        	param.getParams().put(new Integer(idx++), m.group(2));
            //System.out.println(m.group(2));
        }
        String result = sql.replaceAll(regex, "?");
        param.setSql(result);
        return param;
    }
    /**
     * ʹ�ò���ֵMap�����pStat
     * @param pStat
     * @param pMap ����������ֵ�����е�ֵ���ԱȽ�����Ĳ����ࡣ
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
