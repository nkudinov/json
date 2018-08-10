package gson.serialization;

import com.google.gson.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

public class SerializationTest {
    static class A{
        public final String n_a_m_e;
        public final int age;

        public A(String n_a_m_e, int age) {
            this.n_a_m_e = n_a_m_e;
            this.age = age;
        }

        public String getN_a_m_e() {
            return n_a_m_e;
        }

        public int getAge() {
            return age;
        }
    }
    class AJsonSerializer implements JsonSerializer<A> {
        @Override
        public JsonElement serialize(A a, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name",a.getN_a_m_e());
            jsonObject.addProperty("age",a.age);
            return jsonObject;
        }
    }
    @Test
    void simpleSerializationTest(){
        Gson gson = new Gson();
        A a = new A("John",23);
        String json = gson.toJson(a);
        Assertions.assertTrue(json.contains("n_a_m_e"));
        Assertions.assertTrue(json.contains("age"));
   }
    @Test
    void simpleCustomJsonSerializerTest(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(A.class, new AJsonSerializer());
        Gson gson = gsonBuilder.create();
        A a = new A("John",23);
        String json = gson.toJson(a);
        Assertions.assertTrue(json.contains("name"));
        Assertions.assertTrue(json.contains("age"));
    }

}
