package gson.serialization.annotation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationTest {
   static class A{
       @Expose(serialize = true)
       private  String name;

       @Expose(serialize = false)
       private final int age;

       public A(String name, int age) {
           this.name = name;
           this.age = age;
       }
   }
   static class A2 {
       @SerializedName("firstName")
       private  String name;

       @SerializedName("ageInMonths")
       private final int age;

       public A2(String name, int age) {
           this.name = name;
           this.age = age;
       }
   }
   static class A3 {
      @Since(1.2)
      @Until(3.0)
      private String firstName;
      @Since(3.0)
      private String lastName;

       public A3(String firstName, String lastName) {
           this.firstName = firstName;
           this.lastName = lastName;
       }
   }
   @Test
   void AnnotationTest(){
       GsonBuilder builder = new GsonBuilder();
       builder.excludeFieldsWithoutExposeAnnotation();
       Gson gson = builder.create();
       A a = new A("John",23);
       String json = gson.toJson(a);
       Assertions.assertTrue(json.contains("name"));
       Assertions.assertFalse(json.contains("age"));
   }
   @Test
    void SerializedNameTest(){
        Gson gson = new Gson();
        String json = gson.toJson(new A2("John",23));
        Assertions.assertTrue(json.contains("firstName"));
        Assertions.assertTrue(json.contains("ageInMonths"));
    }
    @Test
    void versionTest(){
      GsonBuilder builder = new GsonBuilder();
      builder.setVersion(2.0);
      Gson gson = builder.create();
      A3 a3 = new A3("John","Smith");
      String json = gson.toJson(a3);
      Assertions.assertTrue(json.contains("firstName"));
      Assertions.assertFalse(json.contains("lastName"));
    }
}
