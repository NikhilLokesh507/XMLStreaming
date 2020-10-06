import java.math.BigDecimal;

public class MapperUtils {
    public MapperUtils() {}
    public static Object parseStringObject(String object) {
        try {
            return Integer.parseInt(object);
        }
        catch (Exception ignored) {}
        try {
            return Long.parseLong(object);
        }
        catch (Exception ignored) {}
        try {
            return new BigDecimal(object);
        }
        catch(Exception ignored) {}
        return object;

    }

}
