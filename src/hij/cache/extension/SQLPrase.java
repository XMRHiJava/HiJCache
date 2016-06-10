package hij.cache.extension;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import hij.util.generic.IFuncP1;

/**
 * ����XML
 * @author XuminRong
 *
 */
final class SQLPrase implements IFuncP1<Element, SQLInfo> {

	/* ����SQL��ص�XML
	 */
	@Override
	public SQLInfo handle(Element node) {
		SQLInfo info = new SQLInfo();
		NodeList nodes = node.getChildNodes(); 
		if (nodes == null || nodes.getLength() < 1) {
			return null;
		}		

		String dbtype = node.getAttribute("type");
		if (dbtype == null || dbtype.trim().equals("")) {
			return null;
		}
		info.setDbType(dbtype.toLowerCase());
		
		// ���������ӽڵ�,����SQL���(������ҳʱ,�������������)
		for (int i = 0; i < nodes.getLength(); i++) {
			Node child = nodes.item(i);
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element item = (Element)child;
			if (item.getNodeName().toLowerCase().equals("text")) {
				SQLParams sql = SQLParams.parse(item.getTextContent());
				if (sql == null) {
					continue;
				}
				info.setSql(sql);
			}
			if (item.getNodeName().toLowerCase().equals("count")) {
				SQLParams sql = SQLParams.parse(item.getTextContent());
				if (sql != null) {
					info.setCountSQL(sql);
				}
			}
		}
		
		if (info.getSql() == null || info.getSql().getSql().trim() == "") {
			return null;
		}
		return info;
	}
}
