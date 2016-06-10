package hij.cache.extension;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import hij.cache.HiCacheProxy;
import hij.util.generic.IFuncP1;

/**
 * �����ַ����Ĵ���
 * @author XuminRong
 *
 */
public final class HiStringProxy {

	/**
	 * ���캯��
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
     * ����XML�ļ����µ�����XML����
     * @param path
     * @throws Exception
     */
    public void loadXMLsByFolder(String path) throws Exception {
        impl.loadXMLsByFolder(path);
    }
    
    /**
     * ��û����ֵ
     * 		�ڱ�������,�������쳣,ʹ�ö������,��Ȼ������,�����Լ򻯲���'
     * 		������release��,���к�,�û����������޸�XML�ļ��µ��ļ�
     * @param id
     * @return
     */
    public String getValue(String id){
        return impl.getValue(id);
    }
	HiCacheProxy<String> impl = new HiCacheProxy<String>();
}
