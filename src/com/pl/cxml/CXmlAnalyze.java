package com.pl.cxml;

import android.util.Log;

import com.pl.protocol.Pair;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class CXmlAnalyze {
	private SAXReader reader;
	private Document document;
	private Element root;
	private String path;

	public CXmlAnalyze() {
		reader = new SAXReader();
	}

	public CXmlAnalyze(String XMLpath) {
		reader = new SAXReader();
		OpenXml(XMLpath);
	}

	public CXmlAnalyze(InputStream XMLio) {
		reader = new SAXReader();
		OpenXml(XMLio);
	}

	public int OpenXml(String XMLpath) {
		path = XMLpath;
		try {
			return OpenXml(new FileInputStream(XMLpath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int OpenXml(InputStream XMLio) {
		try {
			document = reader.read(XMLio);
		} catch (DocumentException e) {
			document = DocumentHelper.createDocument();
		}
		root = document.getRootElement();
		return 0;
	}

	public boolean CloseXML() {
		if (path == null || path == "")
			return false;
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter output;
		try {
			output = new XMLWriter(new FileOutputStream(path), format);
			output.write(document);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 关掉文件.发现压根就不用关掉啊！JAVA这是什么垃圾回收机制？
		return true;
	}

	/*
	 * 方法3：获得节点的一个属性
	 * 入参：元素属性名，后面是不定参数，依次可以加module,page,group,_param.（如果为"*"，则表示取得当前目录的所有的元素）
	 * 返回：CXmlData类,每个元素只有一个属性，就是你要获得的属性
	 */
	public CXmlData GetTheAttr(String attrName, String... args) {
		String xPath = GetXPath(args);
		CXmlData xmlD = new CXmlData();
		if (xPath == null) {
			xmlD.AddOneAttr(attrName, root.attributeValue(attrName));
		} else {
			List list = document.selectNodes(xPath + "/@" + attrName);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				xmlD.AddOneElement();
				Attribute attribute = (Attribute) iter.next();
				xmlD.AddOneAttr(attrName, attribute.getValue());
			}
		}
		xmlD.SetDefaultAttrName(attrName);
		return xmlD;
	}

	/*
	 * 方法4：获得节点的所有属性
	 * 入参：元素属性名，后面是不定参数，依次可以加module,page,group,_param.（如果为"*"，则表示取得当前目录的所有的元素）
	 * 返回：CXmlData类,每个元素有这个元素所有的属性。
	 */
	public CXmlData GetAllAttr(String... args) {
		String xPath = GetXPath(args);
		CXmlData xmlD = new CXmlData();
		if (xPath == null)
			GetinfoFromElement(root, xmlD);
		else {
			List list = document.selectNodes(xPath);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				xmlD.AddOneElement();
				Element element = (Element) iter.next();
				GetinfoFromElement(element, xmlD);
				xmlD.AddOneAttr(".", element.getName());
			}
		}
		return xmlD;
	}

	/*
	 * 方法5：修改节点的一个属性
	 * 入参：元素属性名，元素要修改成的值，后面是不定参数，依次可以加module,page,group,param.（如果为"*"
	 * ，则表示取得当前目录的所有的元素） 返回：是否改动成功
	 */
	public boolean ChangeAttr(String attrName, String attrValue, String... args) {
		String xPath = GetXPath(args);
		if (xPath == null) {
			Attribute attrb = root.attribute(attrName);
			if (attrb == null)
				root.addAttribute(attrName, attrValue);
			else
				attrb.setValue(attrValue);
		} else {
			List list = document.selectNodes(xPath);
			if (list.size() == 0) {
				CheckAndAddNodeAndAttr(attrName, attrValue, args);
				return false;
			}
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				Attribute attribute = element.attribute(attrName);
				if (attribute == null)
					element.addAttribute(attrName, attrValue);
				else
					attribute.setValue(attrValue);
			}
		}
		return CloseXML();
	}

	/*
	 * 方法5：判断元素是否存在 入参：你懂的 返回：是否存在这个元素
	 */
	public boolean CheckExit(String... args) {
		String xPath = GetXPath(args);
		if (xPath != null) {
			List list = document.selectNodes(xPath + "/@nodetype");
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 方法6：通过地址,cmd,一个元素 获得M_P名字 入参：地址，cmd,一个元素标号 返回：M_P
	 */
	public Pair<String, String> GetM_PFromAddr_Cmd(String addr, String cmd,
			String oneP) {
		String xPath = GetXPath_Addr_Cmd(addr, cmd, oneP);
		String pName, mName;
		if (xPath != null) {
			List list = document.selectNodes(xPath);
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				Element p = element.getParent().getParent();
				Attribute attribute = p.attribute("name");
				if (attribute == null)
					return null;
				pName = attribute.getValue();

				Element m = p.getParent();
				attribute = m.attribute("name");
				if (attribute == null)
					return null;
				mName = attribute.getValue();
				return new Pair<String, String>(mName, pName);
			}
		}
		return null;
	}

	public CXmlData GetFromAddr_Cmd(String addr, String cmd, String oneP) {
		String xPath = GetXPath_Addr_Cmd(addr, cmd, oneP);
		CXmlData xmlD = new CXmlData();
		if (xPath != null) {
			Log.v("sms", xPath);
			List list = document.selectNodes(xPath);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				xmlD.AddOneElement();
				Element element = (Element) iter.next();
				GetinfoFromElement(element, xmlD);
				xmlD.AddOneAttr(".", element.getName());
			}
			return xmlD;
		}
		return null;
	}

	public boolean DeleteNode(String... args) {// 删除元素,入参你懂的，返回你懂的
		String xPath = GetXPath(args);
		boolean re = true;
		if (xPath == null)
			return false;
		else {
			List list = document.selectNodes(xPath);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				re = re && element.getParent().remove(element);
			}
		}
		return re && CloseXML();
	}

	public boolean DeleteAttr(String attrName, String... args) {// 删除元素一个属性,入参你懂的，返回你懂的
																// 如果你要删除所有的属性，建议先删掉然后再重建
		String xPath = GetXPath(args);
		boolean re = true;
		if (xPath == null || "nodetype" == attrName || null == attrName
				|| "" == attrName || (args.length < 4 && "name" == attrName))
			return false;
		else {
			List list = document.selectNodes(xPath + "[@" + attrName + "]");
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				re = re && element.remove(element.attribute(attrName));
			}
		}
		return re && CloseXML();
	}

	public boolean CheckAndAddNode(String... args) {// 添加元素,入参你懂的，返回你懂的
		return CheckAndAddNodeAndAttr(null, null, args);
	}

	public boolean CheckAndAddNodeAndAttr(String attrName, String attrValue,
			String... args) {// 添加元素,顺带更新一个属性,入参你懂的，返回你懂的
		String xPath;
		if (root == null) {
			root = document.addElement("WebomtRoot");
		}
		Element element = root;
		int i = 0;
		for (i = 1; i <= args.length; ++i) {
			xPath = GetXPath(i, args);
			List list = document.selectNodes(xPath);
			if (list.size() == 0) {
				break;
			}
			Iterator iter = list.iterator();
			element = (Element) iter.next();
		}
		for (; i <= args.length; ++i) {
			switch (i) {
			case 1:
				element = element.addElement("module");
				element.addAttribute("nodetype", "module");
				element.addAttribute("name", args[i - 1]);
				break;
			case 2:
				element = element.addElement("page");
				element.addAttribute("nodetype", "page");
				element.addAttribute("name", args[i - 1]);
				break;
			case 3:
				element = element.addElement("group");
				element.addAttribute("nodetype", "group");
				element.addAttribute("name", args[i - 1]);
				break;
			case 4:
				element = element.addElement("p" + args[i - 1]);
				element.addAttribute("nodetype", "param");
				break;
			case 5:
				element = element.addElement("p" + args[i - 1]);
				element.addAttribute("nodetype", "param");
				break;
			}
		}
		if (attrName != null && attrValue != null)
			element.addAttribute(attrName, attrValue);
		return CloseXML();
	}

	// private//////////////////////////////////////////////////////////////////////////private//
	private String GetXPath(String... args) {
		return GetXPath(args.length, args);
	}

	private String GetXPath(int n, String... args) {
		String re = "";// 如果您要看懂这里的代码，我的建议是传入四~5个参数试一下
		switch (n) {
		case 5:
			re = "/*[@nodetype='param']";
		case 4:
			re = (((args[3] == "*" || args[3] == null || args[3] == "") ? ("/*[@nodetype='param']")
					: ("/p" + args[3] + "[@nodetype='param']")) + re);
		case 3:
			re = ("/*[@nodetype='group'"
					+ ((args[2] == "*" || args[2] == null || args[2] == "") ? "]"
							: (" and @name='" + args[2] + "']")) + re);
		case 2:
			re = ("/*[@nodetype='page'"
					+ ((args[1] == "*" || args[1] == null || args[1] == "") ? "]"
							: (" and @name='" + args[1] + "']")) + re);
		case 1:
			re = ("/*[@nodetype='module'"
					+ ((args[0] == "*" || args[0] == null || args[0] == "") ? "]"
							: (" and @name='" + args[0] + "']")) + re);
			break;
		default:
			return null;
		}
		return "/" + re;
	}

	private String GetXPath_Addr_Cmd(String addr, String cmd, String oneP) {
		String cmd1 = "";
		if (cmd.equals("03")) {
			cmd1 = "02";
		} else if (cmd.equals("b3")) {
			cmd1 = "b2";
		} else if (cmd.equals("b5")) {
			cmd1 = "b4";
		} else {
			cmd1 = cmd;
		}
		String re = "/*[@nodetype='module' and @address='" + addr + "']" + "/*[@nodetype='page' and @ChmCmd='" + cmd1 + "']" + "/*[@nodetype='group']" + "/p" + oneP + "[@nodetype='param']";
		return "/" + re;
	}

	private CXmlData GetinfoFromElement(Element em) {
		CXmlData xmlD = new CXmlData();
		GetinfoFromElement(em, xmlD);
		return xmlD;
	}

	private void GetinfoFromElement(Element em, CXmlData xmlD) {
		for (Iterator i = em.attributeIterator(); i.hasNext();) {
			Attribute attribute = (Attribute) i.next();
			// System.out.println(attribute.getName() + ":" +
			// attribute.getData());
			xmlD.AddOneAttr(attribute.getName(), attribute.getValue());
		}
		if (em.elementIterator().hasNext())
			xmlD.AddOneAttr(".hasChild", "true");
		xmlD.AddOneAttr(".", em.getName());
	}
}
