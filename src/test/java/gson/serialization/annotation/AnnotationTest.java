package gson.serialization.annotation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationTest {
   static class A{
       @Expose(serialize = true)
       public  String name;

       @Expose(serialize = false)
       public final int age;

       public A(String name, int age) {
           this.name = name;
           this.age = age;
       }
   }
   static class A2 {
       @SerializedName("firstName")
       public  String name;

       @SerializedName("ageInMonths")
       public final int age;

       public A2(String name, int age) {
           this.name = name;
           this.age = age;
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
        Assertions.assertFalse(json.contains("ageInMonths"));
    }
}
