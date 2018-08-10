package jackson.readTree;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class readTreeTest {
 @Test
  public void readTreeTest() throws IOException {
     ObjectMapper mapper = new ObjectMapper();
     String json = "{\"name\":\"John\",\"age\":23}";
     JsonNode rootNode = mapper.readTree(json);
     Assertions.assertTrue(rootNode.path("name").asText().equals("John"));

 }
}
