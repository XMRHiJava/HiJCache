package hij.cache.test;

import org.junit.Assert;
import org.junit.Test;

import hij.cache.extension.HiSQLProxy;
import hij.cache.extension.SQLInfo;

public class TestSQLProxy {
	@Test
	public void test_Normal() {
		boolean ret = init("xml/sql");
		Assert.assertTrue(ret);
		Assert.assertTrue(HiSQLProxy.getValue("DATA.COLLATIONS.GETDATASOURCE") != null);
        SQLInfo info = HiSQLProxy.getValue("DATA.INNODBLOCKS.GET8PAGE");
        Assert.assertTrue(info != null);
        Assert.assertTrue(info.getCountSQL() != null);
	}
	
	@Test
    public void test_LoadXMLs_NoXMLFile()   {
		boolean ret = init("xml/sqlNoXMLFile");
		Assert.assertTrue(!ret);
    }
	
	@Test
    public void test_LoadXMLs_NoFolder()   {
		boolean ret = init("xml/sqlNotExist");
		Assert.assertTrue(!ret);
    }
	
	@Test
    public void LoadXMLs_XMLError()  {
		boolean ret = init("xml/sqlXmlError");
		Assert.assertTrue(!ret);
    }
	@Test
    public void LoadXMLs_NodeRepeate()  {
		boolean ret = init("xml/sqlNodeRepeate");
		Assert.assertTrue(!ret);
    }
	
	private String getCurrentPath(){  
		 //当前目录的上级目录路径  
	       String rootPath=getClass().getResource("/").getPath();  
	       int pos = rootPath.lastIndexOf("/", rootPath.length() - 2);
	          
	       return rootPath.substring(0, pos) + "/"; 
	   
	   }
	
	private boolean init(String folder){

		String path = getCurrentPath();
		try {
			HiSQLProxy.loadXMLsByFolder(path + folder);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
