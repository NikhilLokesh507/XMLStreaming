import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class XmlParser {

    public static List<Object> processXML(XmlInputStream xmlInputStream, int depth) throws IOException {
        xmlInputStream.ignoreStartDocument();
        if(depth == 0) {
            String key = xmlInputStream.getLocalName();
            Object obj = ((Map)xmlInputStream.getChildEvents()).get(key);
            System.out.println(obj);
            List<Object> list = new LinkedList<>();
            list.add(obj);
            return list;
        }
        xmlInputStream.moveToNextElement();
        xmlInputStream.next();
        xmlInputStream.removeWhiteSpaces();
        int rowNum = 0;
        List<Object> list = new LinkedList<>();
        while(xmlInputStream.isStartElement()) {
            rowNum++;
            Map<String,Object> attributeMap = xmlInputStream.getAttributeMap();
            xmlInputStream.next();
            xmlInputStream.removeWhiteSpaces();
            Object object = xmlInputStream.getChildEvents();
            if(attributeMap.size() > 0) {
                object = XmlInputStream.createMap(attributeMap,object);
            }
            list.add(object);
            xmlInputStream.next();
            xmlInputStream.removeWhiteSpaces();
        }
        return list;
    }
}
