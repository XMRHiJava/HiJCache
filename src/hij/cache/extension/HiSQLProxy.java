package hij.cache.extension;

import hij.cache.HiCacheProxy;

public final class HiSQLProxy {
	
    /**
     * 载入XML文件夹下的所有XML数据
     * @param path
     * @throws Exception
     */
    public static void loadXMLsByFolder(String path) throws Exception {
    	getImpl().loadXMLsByFolder(path);
    }
    
    /**
     * 获得缓存的值
     * 		在本函数中,隐藏了异常,使用断言替代,虽然不完美,但可以简化操作'
     * 		假设在release下,运行后,用户不会轻易修改XML文件下的文件
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
