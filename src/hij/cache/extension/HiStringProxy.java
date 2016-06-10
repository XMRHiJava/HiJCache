package hij.cache.extension;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import hij.cache.HiCacheProxy;
import hij.util.generic.IFuncP1;

/**
 * 缓存字符串的代理
 * @author XuminRong
 *
 */
public final class HiStringProxy {

	/**
	 * 构造函数
	 */
	public HiStringProxy(){
		impl.setParse(new IFuncP1<Element, String>(){

			@Override
			public String handle(Element node) {
				NodeList nodes = node.getChildNodes(); 
				if (nodes == null || nodes.getLength() < 1) {
					return null;
				}
				
				for (int i = 0; i < nodes.getLength(); i++) {
					Node child = nodes.item(i);
					if (child.getNodeName().toLowerCase() == "text") {
						return child.getNodeValue();
					}
			}
			return null;
		} });
	}
	
    /**
     * 载入XML文件夹下的所有XML数据
     * @param path
     * @throws Exception
     */
    public void loadXMLsByFolder(String path) throws Exception {
        impl.loadXMLsByFolder(path);
    }
    
    /**
     * 获得缓存的值
     * 		在本函数中,隐藏了异常,使用断言替代,虽然不完美,但可以简化操作'
     * 		假设在release下,运行后,用户不会轻易修改XML文件下的文件
     * @param id
     * @return
     */
    public String getValue(String id){
        return impl.getValue(id);
    }
	HiCacheProxy<String> impl = new HiCacheProxy<String>();
}
