package jackson.array;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Array;

class A{
   public String name;
   public int age;
    @JsonCreator
    public A(@JsonProperty("name") String name,@JsonProperty("age") int age) {
        this.name = name;
        this.age = age;
    }
}
public class ArrayTest {
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
       mapper = new ObjectMapper();
    }

    @Test
    void writeTest() throws JsonProcessingException {
      A[] arr = new A[2];
      arr[0] = new A("John",23);
      arr[1] = new A("Ivan",23);
      String json = mapper.writeValueAsString(arr);
        System.out.println(json);
      Assertions.assertTrue(json.contains("[{"));
      Assertions.assertTrue(json.contains("John"));
      Assertions.assertTrue(json.contains("Ivan"));
    }
    @Test
    void readTest() throws IOException {
        String json = "[{\"name\":\"John\",\"age\":23},{\"name\":\"Ivan\",\"age\":23}]";
       A[] arr =  mapper.readValue(json,A[].class);
       Assertions.assertTrue(arr.length == 2);
    }
}
