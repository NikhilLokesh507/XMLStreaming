import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class TestXmlInputStream {
    @Test
    public void test_processXML() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource("big.json").getFile());
        List<Object> list = XmlParser.processXML(new XmlInputStream(new FileInputStream(classLoader.getResource("big.xml").getFile())), 1);
        List<Object> list1 = getJsonList(file);
        Assert.assertTrue(list.equals(list1));
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
