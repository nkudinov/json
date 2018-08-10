package gson.deserialization;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeserializationTest {
 static class A{
     public final String name;
     public final int age;

     public A(String name, int age) {
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
 @Test
 void stringDeserializationTest(){
   Gson gson = new Gson();
   String str = gson.fromJson("\"John\"",String.class);
     Assertions.assertEquals("John",str);
 }
 @Test
 void deserialization1Test() {
    String json = "{\"name\":\"John\",\"age\":23}";
    Gson gson = new Gson();
    A a  = gson.fromJson(json,A.class);
    Assertions.assertEquals(a.getAge(),23);
    Assertions.assertEquals(a.getName(),"John");
 }
}
