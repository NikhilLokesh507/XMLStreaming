import javafx.util.Pair;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            staxStreaming();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void staxStreaming() throws FileNotFoundException, XMLStreamException {
        XMLInputFactory xmif = XMLInputFactory.newInstance();
        XMLStreamReader xmlr = xmif.createXMLStreamReader(new FileInputStream("/Users/nikhil/Documents/Hevo/xmltests/src/main/resources/student.xml"));
        while(xmlr.getEventType() != XMLStreamConstants.START_ELEMENT) {
            xmlr.next();
        }
        xmlr.next();
        while(xmlr.getEventType() != XMLStreamConstants.START_ELEMENT) {
            xmlr.next();
        }
        try {
            Map<String, List<Object>> map = (Map<String, List<Object>>) getChildEvents(xmlr);
            System.out.println(map);
        } catch(Exception e) {}

    }


    private static Object getChildEvents(XMLStreamReader xmlr) throws XMLStreamException {
        if(xmlr.getEventType() == XMLStreamConstants.CHARACTERS) {
            Object obj = getChildEvent(xmlr);
            if(xmlr.hasNext()) xmlr.next();
            return obj;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        while(xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
            if(xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                String key = xmlr.getLocalName();
                int attributeCount = xmlr.getAttributeCount();
                List<Pair<String,Object>> attributes = new LinkedList<>();
                for(int i=0; i<attributeCount; i++) {
                    String attibuteKey = xmlr.getAttributeLocalName(i);
                    Object object = MapperUtils.parseStringObject(xmlr.getAttributeValue(i));
                    attributes.add(new Pair<>(attibuteKey, object));
                }
                xmlr.next();
                removeWhiteSpaces(xmlr);
                Object obj = getChildEvents(xmlr);


                if(attributeCount>0) {
                    obj = createMap(xmlr,attributes,obj,key);
                }
                addToMap(map,key,obj);
                xmlr.next();
                if(xmlr.getEventType() == XMLStreamConstants.CHARACTERS && xmlr.isWhiteSpace()) {
                    xmlr.next();
                }
            }
        }

        return map;
    }

    /**
     * this method helps in converting the xml attributes into elements
     * @param xmlr
     * @param attributes
     * @param object
     * @param key
     * @return
     */
    public static Map<String, Object> createMap(XMLStreamReader xmlr, List<Pair<String,Object>> attributes, Object object, String key) {
        if(object instanceof LinkedHashMap) {
            for(Pair<String,Object> pair : attributes) {
                ((LinkedHashMap<String,Object>) object).put(pair.getKey(), pair.getValue());
            }
        }
        else {
            Map<String,Object> map = new LinkedHashMap<>();
            for(Pair<String,Object> pair : attributes) {
                ((LinkedHashMap<String,Object>) object).put(pair.getKey(), pair.getValue());
            }
            return map;
        }
        return ((LinkedHashMap)object);
    }

    /**
     * Adds the <String key, Object value> pair into a map;
     * @param map
     * @param key
     * @param obj
     */
    public static void addToMap(Map<String, Object> map, String key, Object obj) {
        if(map.containsKey(key)) {
            Object valueObj = map.get(key);
            if(valueObj instanceof List) {
                ((List<Object>) valueObj).add(obj);
            }
            else {
                List<Object> list = new LinkedList<>();
                list.add(valueObj);
                list.add(obj);
                map.put(key, list);
            }
        }
        else {
            map.put(key, obj);
        }
    }

    /**
     * Removes uncessary whitespaces between start and end elements
     * @param xmlr
     * @throws XMLStreamException
     */
    private static void removeWhiteSpaces(XMLStreamReader xmlr) throws XMLStreamException {
        if(xmlr.getEventType() == XMLStreamConstants.CHARACTERS && xmlr.isWhiteSpace()) {
            xmlr.next();
        }
        return;
    }


    /**
     * Method returns relevant Object if the event is 'CHARACTERS'.
     * @param xmlr
     * @return
     * @throws XMLStreamException
     */
    private static Object getChildEvent(XMLStreamReader xmlr) throws XMLStreamException {
        String ans = (new String(xmlr.getTextCharacters(), xmlr.getTextStart(), xmlr.getTextLength())).trim();
        if(ans.equals("")) {
            return null;
        }
        else return MapperUtils.parseStringObject(ans);
    }
}
