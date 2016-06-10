package hij.cache.extension;

import hij.cache.HiCacheProxy;

public final class HiSQLProxy {
	
    /**
     * ����XML�ļ����µ�����XML����
     * @param path
     * @throws Exception
     */
    public static void loadXMLsByFolder(String path) throws Exception {
    	getImpl().loadXMLsByFolder(path);
    }
    
    /**
     * ��û����ֵ
     * 		�ڱ�������,�������쳣,ʹ�ö������,��Ȼ������,�����Լ򻯲���'
     * 		������release��,���к�,�û����������޸�XML�ļ��µ��ļ�
     * @param id
     * @return
     */
    public static SQLInfo getValue(String id){
        return getImpl().getValue(id);
    }
    
	static HiCacheProxy<SQLInfo> impl = null;
	
	private static HiCacheProxy<SQLInfo> getImpl() {
		if (impl == null) {
			impl = new HiCacheProxy<SQLInfo>();
			impl.setParse(new SQLPrase());
		}
		return impl;
	}
}
