package hij.cache.extension;

import org.junit.Assert;
import org.junit.Test;

public class TestCacheProxy {

	@Test
	public void test_IO() {
		String path = System.getProperty("user.dir");
		Assert.assertNotNull(path);
		path += "/xml/sql";
		try {
			HiSQLProxy.loadXMLsByFolder(path);
			SQLInfo info = HiSQLProxy.getValue("DATA.CHARACTERSETS.ADD");
			Assert.assertNotNull(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertTrue(e.getMessage(), false);
		}
	}	

	@Test
	public void test_SQLParams_parse() {
		String sql = "Select @a @b,@c>,@c>,@d<,@e!,@f),'@g',@h\r\n,@i-,@j+,@k/, @l";
		SQLParams params = SQLParams.parse(sql);
		
		Assert.assertEquals("Select ? ?,?>,?>,?<,?!,?),'?',?\r\n,?-,?+,?/, ?", params.getSql());
		Assert.assertEquals(params.getParams().get(3), "c");
		Assert.assertEquals(params.getParams().get(4), "c");
	}
}
