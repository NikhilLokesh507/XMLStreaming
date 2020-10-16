import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class TestXmlInputStream {
    @Test
    public void test_processXML() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<Pair<String,Integer>> fileList = new LinkedList<>();
        fileList.add(new Pair<>("student", 1));
        fileList.add(new Pair<>("level0", 0));
        fileList.add(new Pair<>("big", 1));
        for(Pair<String,Integer> pair : fileList) {
            File jsonFile = new File(classLoader.getResource(String.format("%s.json", pair.getKey())).getFile());
            File xmlFile = new File(classLoader.getResource(String.format("%s.xml", pair.getKey())).getFile());
            if(pair.getValue() == 0) {
                Assert.assertTrue(XmlParser.processXML(new XmlInputStream(new FileInputStream(xmlFile)), pair.getValue()).get(0).equals(getJsonMap(jsonFile)));
                continue;
            }
            Assert.assertTrue(XmlParser.processXML(new XmlInputStream(new FileInputStream(xmlFile)), pair.getValue()).equals(getJsonList(jsonFile)));
        }

    }

    private static Object clean(Object object) {
        if(object instanceof List) {
            List<Object> list = new LinkedList<>();
            for(Object item : (List)object) {
                list.add(clean(item));
            }
            return list;
        } else if(object instanceof Map) {
            Map<String,Object> map = new LinkedHashMap<>();
            Map<String,Object> mapObject = (Map) object;
            for(Map.Entry entry : mapObject.entrySet()) {
                map.put((String)entry.getKey(), clean(entry.getValue()));
            }
            return map;
        }
        else if(object instanceof String)
            return MapperUtils.parseStringValue((String)object);
        return null;
    }

    private static List getJsonList(File file) throws IOException {
        String jsonString = getJsonString(new BufferedReader(new FileReader(file)));
        ObjectMapper mapper = new ObjectMapper();
        List<Object> list = (List<Object>) clean(mapper.readValue(jsonString, List.class));
        return list;
    }

    private static Map getJsonMap(File file) throws IOException {
        String jsonString = getJsonString(new BufferedReader(new FileReader(file)));
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = (Map) clean(mapper.readValue(jsonString, Map.class));
        return map;
    }

    private static String getJsonString (BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = "";
        line = reader.readLine();
        while(line != null) {
            sb.append(line);
            line = reader.readLine();
        }
        return sb.toString();
    }
}
