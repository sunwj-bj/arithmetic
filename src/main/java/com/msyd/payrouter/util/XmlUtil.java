package com.msyd.payrouter.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Xml处理工具
 * <p> Copyright: Copyright (c) 2014 </p>
 * <p> Create Date: 2014-5-10 </p>
 * <p> Company: msdzsw.com </p> 
 * @author zhangqiang@minshengec.cn
 * @version $Id: XmlUtil.java,v 1.0 zq Exp $
 */
public class XmlUtil {

    private static final Log log = LogFactory.getLog(XmlUtil.class);

    public static Node getXmlNode(String xml, String path) throws Exception {

        SAXReader saxReader = new SAXReader();
        //解析报文头
        Node node;

        Document doc = saxReader.read(new InputSource(new StringReader(xml)));
        node = doc.selectSingleNode(path);

        return node;
    }

    public static String replaceSpace(String xml) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(xml);
        return m.replaceAll("");
    }

    /**
     * 将xml字符串转换成map
     * xml必须满足条件，任意节点下子节点名称要么全部相同，要么全部不相同。全部相同将转为list
     * @author wangzy
     * @param xml
     * @return Map
     */
    public static Map<String, Object> xmlToMap(String xml) {
        log.info("解析xml开始");
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Document doc = DocumentHelper.parseText(xml);
            // 获取根节点
            Element rootElt = doc.getRootElement(); // 取根节点

            ele2map(map, rootElt);
        } catch (Exception e) {
            log.error("解析xml异常", e);
        }
        log.debug(map);
        log.info("解析xml结束");
        return map;
    }

    /***
     * 递归调用解析XML
     * 
     * @param map
     * @param ele
     */
    public static void ele2map(Map<String, Object> map, Element ele) {
        // 获得当前节点的子节点
        List<Element> elements = ele.elements();
        if (elements.size() == 0) {
            // 没有子节点说明当前节点是叶子节点，直接取值即可
            map.put(ele.getName(), ele.getText());
        } else if (elements.size() == 1) {
            // 只有一个子节点说明不用考虑list的情况，直接继续递归即可
            Map<String, Object> tempMap = new HashMap<String, Object>();
            ele2map(tempMap, elements.get(0));
            map.put(ele.getName(), tempMap);
        } else {
            // 多个子节点的话就得考虑是否为list
            if (elements.get(0).getName().equals(elements.get(1).getName())) {
                // 前两个子节点名称相同，认为是list
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Element element : elements) {
                    Map<String, Object> tempMap1 = new HashMap<String, Object>();
                    ele2map(tempMap1, element);
                    list.add(tempMap1);
                }
                map.put(ele.getName(), list);
            } else {
                // 前两个子节点名称不相同，不认为是list
                Map<String, Object> tempMap1 = new HashMap<String, Object>();
                for (Element element : elements) {
                    ele2map(tempMap1, element);
                }
                map.put(ele.getName(), tempMap1);
            }
        }
    }

    //    public static void main(String[] args) {
    //        String xml = "<root><head><rsp_dt>20150831</rsp_dt><rsp_tm>134919</rsp_tm><req_id>20150831134248731</req_id><rsp_id>20150831134919431</rsp_id><msg_cd>000000</msg_cd><msg_inf>上传文件成功,创建格式校验任务成功,任务编号:[1751]</msg_inf></head><content><msg>0</msg><msg>1</msg><msg>2</msg></content></root>";
    //        Map<String, Object> m = xmlToMap(xml);
    //        System.out.println(m);
    //    }
}
