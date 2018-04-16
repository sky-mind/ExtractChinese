package com.flower.extractlib;

/**
 * Created by nicolee on 2016/9/28.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class DomParser {

    public static Map<String,String> parse(InputStream is) throws Exception {
//        List<Map<String,String>> List = new ArrayList<Map<String,String>>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例
        DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例
        Document doc = builder.parse(is);   //解析输入流 得到Document实例
        Element rootElement = doc.getDocumentElement();
        Map<String,String> map = new HashMap<>();
        NodeList items = rootElement.getElementsByTagName("string");
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            String attrVal = item.getAttributes().getNamedItem("name").getNodeValue();
            NodeList properties = item.getChildNodes();
            for (int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                String nodeValue = property.getNodeValue();
                System.out.println("attrVal:"+attrVal + ", property.getNodeValue() :" + property.getNodeValue());
                map.put(nodeValue, attrVal);
            }
//            List.add(map);
        }
        return map;
    }


}