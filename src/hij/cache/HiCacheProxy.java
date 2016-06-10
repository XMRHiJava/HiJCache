package hij.cache;

import org.w3c.dom.Element;

import hij.cache.impl.CacheMngImpl;
import hij.util.generic.IFuncP1;

/**
 * �����ļ���������XML�ļ�����Ϣ
 * Ŀ��:
 * 	1: ���������߼��޹صĶ����Ӵ������Ƴ���XML��,����SQL���(����νԪ���)
 *  2: XML�иĶ�,���������������,������
 *  3: �����û��Լ���չҪ�������Ϣ(ֻҪ����Ҫ��)
 * Ҫ��:
 * 	1: XML�ļ���.XML��Ϊ��׺(��Сд������)
 *  2: ������Ԫ��,
 *  		��һ��Ϊ��Ԫ��,
 *  		�ڶ�����Ҫ�Ƿ�����,�������κ��߼�,��Ԫ����Ϣ������,Ҳ�������������
 *  		����������Ҫ��Ϣ�洢��,Ҫ����������Ԫ�ض���һ������Ϊ"id"��Ԫ��,��ȫ��Ψһ
 * 
 * @author ������
 *
 * @param <T>
 */
public class HiCacheProxy<T> {
	
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
    public T  getValue(String id){
        return impl.getValue(id);
    }
    
    /**
     * ����XML��������
     * @param parse
     */
    public void setParse(IFuncP1<Element, T> parse){
    	impl.setParse(parse);
    }
    
    CacheMngImpl<T> impl = new CacheMngImpl<T>();
}
