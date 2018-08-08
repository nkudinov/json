package jackson.map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapTest {
   @Test
   public void mapTest1() throws IOException {
       ObjectMapper mapper = new ObjectMapper();
       String json = "{\"name\":\"John\",\"age\":23}";
       Map<String,String> map= mapper.readerFor(HashMap.class).readValue(json);
       Assertions.assertTrue(map.containsKey("name"));
       Assertions.assertTrue(map.containsKey("age"));
   }
    @Test
    public void mapTest2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\",\"age\":23}";
        Map<String,String> map= mapper.readValue(json,new TypeReference<HashMap<String,String>>() {});
        Assertions.assertTrue(map.containsKey("name"));
        Assertions.assertTrue(map.containsKey("age"));
    }
}
