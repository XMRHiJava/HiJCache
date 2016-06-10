package hij.cache.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import hij.cache.HiCacheData;
import hij.util.HiLog;
import hij.util.generic.IFuncP1;

/**
 * 缓存XML文件夹下的数据
 * @author XuminRong
 *
 * @param <T>
 */
public class CacheMngImpl<T>  {

    /**
     * 载入XML文件夹下的所有XML数据
     * @param path
     * @throws Exception
     */
    public void loadXMLsByFolder(String path) throws Exception {
    	if (parse == null) {
            HiLog.error("not set xml parse object, so can,t chahe data");            
            throw new Exception("not set xml parse object, so can,t chahe data");
    	}
    	
        this.folder = path;
        readXMLFiles(path);
        lastTime = getLastTime();
    }
    
    /**
     * 获得缓存的值
     * 		在本函数中,隐藏了异常,使用断言替代,虽然不完美,但可以简化操作'
     * 		假设在release下,运行后,用户不会轻易修改XML文件下的文件
     * @param id
     * @return
     */
    public T  getValue(String id){
        if (isFolderChanged()){
            try {
				readXMLFiles(folder);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assert(false);
				return null;
			}
        }
        if (!dataCaches.containsKey(id)) {
        	return null;
        }
        return dataCaches.get(id).getData();
    }
    
    /**
     * 设置XML解析对象
     * @param parse
     */
    public void setParse(IFuncP1<Element, T> parse){
    	this.parse = parse;
    }
    
    /**
     * 解析并缓存文件夹下的XML文件
     * @param path
     * @throws Exception
     */
    private void readXMLFiles(String path) throws Exception {
        File dir = new File(path);
        if (!dir.exists()) {
            HiLog.error("(%s) folder is not exist,please check you xml folder.", 
            		path);
            
            throw new Exception(String.format(
            		"(%s) folder is not exist,please check you xml folder.",
            		path));
        }
        
        if (!dir.isDirectory()) {
            HiLog.error("(%s) is not folder,please check.", path);
            
            throw new Exception(String.format(
            		"(%s) is not folder,please check.",
            		path));
        }
        
        files.clear();
        dataCaches.clear();
        File[] fls = dir.listFiles();
        for (int i = 0; i < fls.length; i++) {
            if (!fls[i].getName().toLowerCase().endsWith(".xml")) {
                continue;
            }

            files.add(fls[i]);
        }

        if (files.size() < 1) {
            HiLog.error("folder (%s) not include xml files", path);
            
            throw new Exception(String.format(
            		"folder (%s) not include xml files", 
            		path));
        }

        for (int i = 0; i < files.size(); i++) {
            readXMLFile(files.get(i));
        }
    }

    /**
     * 解析一个XML文件
     * @param file
     * @throws Exception
     */
    private void readXMLFile(File file) throws Exception{
    	
		Document document = null;		
		String errorMsg = "";
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(file.getPath());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorMsg = e.getMessage();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorMsg = e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorMsg = e.getMessage();
		} 
		
		if (errorMsg != "") {
            HiLog.error(
            		"parse xml file(%s)faile,script:%s", 
            		file.getPath(), errorMsg);
            throw new Exception(String.format("parse xml file(%s)faile,script:%s", 
            		file.getPath(), errorMsg));
		}
		Element root = document.getDocumentElement();//根目录对象
		NodeList nodeList = root.getChildNodes(); 
		for (int i = 0; i < nodeList.getLength(); i++) { // Module
			Node node = nodeList.item(i); 
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			NodeList childs = node.getChildNodes(); 
			for (int j = 0; j < childs.getLength(); j++) { // 具体缓存对象
				Node child = childs.item(j); 
				if (child.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				Element item = (Element)child;				
				onElement(item, file);
			} 
		} 
    }
    
    /**
     * 解析一个第三级元素(要缓存的元素)
     * @param item
     * @param file
     * @throws Exception
     */
    private void onElement(Element item, File file) throws Exception {
		String id = item.getAttribute("id");
		if (id == null || id.trim().equals("")) {
			return;
		}
		
		HiCacheData<T> cacheData = dataCaches.get(id);
		if (cacheData != null) {
            HiLog.error(
            		"key(%s) is alread in file(%s), so  in file(%s) sencond time is error", 
            		id, cacheData.getFile(), file.getPath());
            
            throw new Exception(String.format(
            		"key(%s) is alread in file(%s), so  in file(%s) sencond time is error", 
            		id, cacheData.getFile(), file.getPath()));
		}
		
		if (parse == null) {
			return;
		}
		cacheData = new HiCacheData<T>();
		T t = parse.handle(item);
		if (t == null) {
			return;
		}
		cacheData.setData(t);
		cacheData.setFile(file.getPath());
		dataCaches.put(id, cacheData);
    }

    /**
     * 文件夹下文件是否被修改
     * @return
     */
    private boolean isFolderChanged() {
        long dt = getLastTime();
        if (dt <= lastTime) {
            return false;
        } else{
        	lastTime = dt;
            return true;
        }
    }
    
    /**
     * 取得文件夹下文件的最后修改事件
     * 		由于对File相关机制不熟悉,基于效率考虑,没有动态判断XML的增减情况
     * @return
     */
    private long getLastTime(){
        long dt = lastTime;
        for (int i = 0; i < files.size(); i++){
        	if(!files.get(i).exists()) {
        		continue;
        	}
            long t = files.get(i).lastModified();
            if (t > dt) {
                dt = t;
            }
        }
        return dt;
    }
    
    
    /**
     * XML解析对象
     */
    IFuncP1<Element, T> parse = null;
    
    /**
     * 文件夹下XML的最后修改时间
     */
    long lastTime = 0;
    
    
    /**
     * 包含XML文件的文件夹
     */
    String folder = "";
    List<File> files = new ArrayList<File>();
    Map<String, HiCacheData<T>> dataCaches = new HashMap<String, HiCacheData<T>>();
}

