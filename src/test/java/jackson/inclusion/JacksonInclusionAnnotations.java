package jackson.inclusion;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JacksonInclusionAnnotations {
  ObjectMapper mapper;
  @JsonIgnoreProperties("age")
  static class A1{
   private final String name;
   private final int age;

     public A1(String name, int age) {
         this.name = name;
         this.age = age;
     }

      public String getName() {
          return name;
      }

      public int getAge() {
          return age;
      }
  }

  static class A2{
      private final String name;
      private final int age;

      public A2(String name, int age) {
          this.name = name;
          this.age = age;
      }
      @JsonIgnore
      public String getName() {
          return name;
      }

      public int getAge() {
          return age;
      }
  }
  static class A3{
      public int age;
      public Name name;

      public A3(int age, Name name) {
          this.age = age;
          this.name = name;
      }
      @JsonIgnoreType
      static class Name {
          public String firstName;
          public String lastName;

          public Name(String firstName, String lastName) {
              this.firstName = firstName;
              this.lastName = lastName;
          }
      }
  }
  @JsonInclude(JsonInclude.Include.NON_NULL)
  static class A4 {
      private String name;
      private int age;

      public A4(String name, int age) {
          this.name = name;
          this.age = age;
      }

      public String getName() {
          return name;
      }

      public int getAge() {
          return age;
      }
  }
  @JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
  static class A5 {
      private final String name;
      private final int age;

      public A5(String name, int age) {
          this.name = name;
          this.age = age;
      }
  }
  @BeforeEach
  void setUp() {
      mapper = new ObjectMapper();
  }

    @Test
    void JsonIgnorePropertiesTest() throws JsonProcessingException {
      A1 a1 = new A1("John",23);
      String json = mapper.writeValueAsString(a1);
      Assertions.assertTrue(json.contains("John"));
      Assertions.assertFalse(json.contains("23"));
    }
    @Test
    void JsonIgnoreTest() throws JsonProcessingException {
        A2 a2 = new A2("John",23);
        String json = mapper.writeValueAsString(a2);
        Assertions.assertFalse(json.contains("John"));
        Assertions.assertTrue(json.contains("23"));

    }
    @Test
    void JsonIgnoreTypeTest() throws JsonProcessingException {
     A3.Name name = new A3.Name("John","smith");
     A3 a3 = new A3(23,name);
     String json = mapper.writeValueAsString(a3);
     Assertions.assertFalse(json.contains("John"));
    }

    @Test
    void JsonIncludeTest() throws JsonProcessingException {
        A4 a4 = new A4(null,23);
        String json = mapper.writeValueAsString(a4);
        Assertions.assertTrue(json.contains("23"));
        Assertions.assertFalse(json.contains("null"));
    }
    @Test
    void JsonAutoDetectTest() throws JsonProcessingException {
      A5 a5 = new A5("John",23);
      String json = mapper.writeValueAsString(a5);
      Assertions.assertTrue(json.contains("23"));
      Assertions.assertTrue(json.contains("John"));

    }
}
