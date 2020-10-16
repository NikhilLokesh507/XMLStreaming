import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XmlInputStream extends InputStream {

    private XMLStreamReader xmlStreamReader;
    public XmlInputStream(InputStream is) throws IOException {
        try {
            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(is);
        } catch(XMLStreamException x) {
            throw new IOException(x);
        }

    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {
        try {
            xmlStreamReader.close();
        }catch(XMLStreamException x) {
            throw new IOException(x);
        }
    }

    public void next() throws IOException {
        try {
            xmlStreamReader.next();
        } catch(XMLStreamException | NoSuchElementException x) {
            throw new IOException(x);
        }
    }

    public String getLocalName() {
        return xmlStreamReader.getLocalName();
    }

    public void ignoreStartDocument() throws IOException {
        while(xmlStreamReader.getEventType() == XMLStreamConstants.START_DOCUMENT) {
            next();
        }
        removeWhiteSpaces();
    }
    public void removeWhiteSpaces() throws IOException{
        while(xmlStreamReader.isWhiteSpace() || xmlStreamReader.getEventType() == XMLStreamConstants.COMMENT) {
            next();
        }
    }
    public void moveToNextElement() throws IOException {
        while(xmlStreamReader.getEventType() != XMLStreamConstants.START_ELEMENT) {
            next();
        }
    }

    public boolean hasNext() throws IOException {
        try {
            return xmlStreamReader.hasNext();
        } catch(XMLStreamException x) {
            throw new IOException(x);
        }
    }


    public Object getChildEvents() throws IOException {
        if(xmlStreamReader.hasText() || (xmlStreamReader.getEventType() == XMLStreamConstants.CDATA)) {
            Object obj = parseElement();
            if(hasNext())
                next();
            if(obj != null)
                return obj;
        }

        Map<String, Object> map = new LinkedHashMap<>();
        while(isStartElement()) {
            String key = xmlStreamReader.getLocalName();
            Map<String,Object> attributeMap = getAttributeMap();
            next();
            removeWhiteSpaces();
            Object object = getChildEvents();
            removeWhiteSpaces();
            if(attributeMap.size() > 0) {
                object = createMap(attributeMap,object);
            }
            addToMap(map,key,object);
            next();
            removeWhiteSpaces();
        }
        return map;
    }


    public Map<String,Object> getAttributeMap() throws IOException {
        if(xmlStreamReader.isStartElement()) {
            int attributeCount = xmlStreamReader.getAttributeCount();
            Map<String,Object> attributeMap = new LinkedHashMap();
            for(int attributeIndex = 0; attributeIndex < attributeCount; attributeIndex++){
                String key = "attribute_" + xmlStreamReader.getAttributeLocalName(attributeIndex);
                Object object = MapperUtils.parseStringValue(xmlStreamReader.getAttributeValue(attributeIndex).trim());
                attributeMap.put(key,object);
            }
            return attributeMap;
        }
        else {
            throw new IOException("current element is not a start element");
        }
    }

    public Object parseElement() throws IOException {
        Object object = null;
        if(xmlStreamReader.isCharacters() || xmlStreamReader.getEventType() == XMLStreamConstants.CDATA) {
            object = MapperUtils.parseStringValue(new String(xmlStreamReader.getTextCharacters(), xmlStreamReader.getTextStart(), xmlStreamReader.getTextLength()).trim());
            object = (object.equals("") ? null : object);
        }
        removeWhiteSpaces();
        return object;
    }

    public boolean isStartElement() {
        return xmlStreamReader.isStartElement();
    }

    public static Map<String,Object> createMap(Map<String,Object> attributeMap, Object object) {
        Map<String,Object> map = new LinkedHashMap<>();
        if(object instanceof Map){
            for(Map.Entry<String,Object> entry : ((Map<String,Object>) object).entrySet()) {
                addToMap(map, entry.getKey(), entry.getValue());
            }
        }
        else {
            map.put("value",object);
        }
        for(Map.Entry<String,Object> entry : attributeMap.entrySet()) {
            addToMap(map, entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static void addToMap(Map<String,Object> map, String key, Object object) {
        if(map.containsKey(key)) {
            Object valueObj = map.get(key);
            if(valueObj instanceof List) {
                ((List<Object>) valueObj).add(object);
            }
            else {
                List<Object> list = new LinkedList<>();
                list.add(valueObj);
                list.add(object);
                map.put(key,list);
            }
        }
        else {
            map.put(key,object);
        }
    }

}
