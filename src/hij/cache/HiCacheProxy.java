package hij.cache;

import org.w3c.dom.Element;

import hij.cache.impl.CacheMngImpl;
import hij.util.generic.IFuncP1;

/**
 * 缓存文件夹下所有XML文件的信息
 * 目标:
 * 	1: 将所有与逻辑无关的东西从代码中移出到XML中,例如SQL语句(即所谓元编程)
 *  2: XML有改动,本库能立即察觉到,并更新
 *  3: 允许用户自己扩展要缓存的信息(只要符合要求)
 * 要求:
 * 	1: XML文件以.XML作为后缀(大小写不敏感)
 *  2: 有三级元素,
 *  		第一级为根元素,
 *  		第二级主要是分区域,不参与任何逻辑,该元素信息不解析,也不缓存相关属性
 *  		第三级是主要信息存储区,要求所有三级元素都有一个名称为"id"的元素,且全局唯一
 * 
 * @author 徐敏荣
 *
 * @param <T>
 */
public class HiCacheProxy<T> {
	
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
    public T  getValue(String id){
        return impl.getValue(id);
    }
    
    /**
     * 设置XML解析对象
     * @param parse
     */
    public void setParse(IFuncP1<Element, T> parse){
    	impl.setParse(parse);
    }
    
    CacheMngImpl<T> impl = new CacheMngImpl<T>();
}
