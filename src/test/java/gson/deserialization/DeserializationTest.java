package gson.deserialization;

import com.google.gson.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

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
 static class AJsonDeserializer implements JsonDeserializer<A> {
     @Override
     public A deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         String name = jsonObject.get("n-a-m-e").getAsString();
         int age     = jsonObject.get("a-g-e").getAsInt();
         return new A(name,age);
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
 @Test
  void simpleJsonDeserializerTest(){
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(A.class,new AJsonDeserializer());
    String json = "{\"n-a-m-e\":\"John\",\"a-g-e\":23}";
    Gson gson = gsonBuilder.create();
    A a  = gson.fromJson(json,A.class);
    Assertions.assertEquals(a.getAge(),23);
    Assertions.assertEquals(a.getName(),"John");
 }
}
