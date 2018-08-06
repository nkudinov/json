package jackson.serialization;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

class MyDateSerializer extends StdSerializer<LocalDate> {
    private static DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public MyDateSerializer() {
        this(null);
    }

    public MyDateSerializer(Class<LocalDate> t) {
        super(t);
    }
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.format(formatter));
        }

}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JacksonSerializationAnnotations {
    ObjectMapper mapper;
    static class A1 {
        private final Map<String,String> values;
        public A1(Map<String,String> name) {
            this.values = name;
        }
        @JsonAnyGetter
        public Map<String,String> getValue() {
            return values;
        }
    }
    static class A2 {
        private final String  name;
        public A2(String name) {
            this.name = name;
        }
        @JsonGetter("firstName")
        public String getName() {
            return name;
        }
    }
    @JsonPropertyOrder({"age","name"})
    static class A3{
        private final String name;
        private final int age;

        public A3(String name, int age) {
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
    static class A4{

        public final String name;

        public A4(String name) {
            this.name = name;
        }
        @JsonRawValue
        public String getName() {
            return name;
        }
    }
    static class A5 {

        private final String name;
        private final int age;

        public A5(String name, int age) {
            this.name = name;
            this.age = age;
        }
        @JsonValue
        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
    @JsonRootName("Person")
    static class A6{
        private final String name;
        private final int age;

        public A6(String name, int age) {
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
    static class A7{
        private final String name;
        private final LocalDate date;

        public A7(String name, LocalDate date) {
            this.name = name;
            this.date = date;
        }

        public String getName() {
            return name;
        }
        @JsonSerialize(using=MyDateSerializer.class)
        public LocalDate getDate() {
            return date;
        }
    }

    @BeforeAll
    public void SetUp(){
        mapper =  new ObjectMapper();
    }
    @Test
    public  void JsonAnyGetterTest() throws JsonProcessingException {

        Map<String,String> m = new HashMap<String,String>(){{ put("1","one"); put("2","two");}};

        String json = mapper.writeValueAsString(new A1(m));

        Assertions.assertTrue(json.contains("\"1\":\"one\""));
        Assertions.assertTrue(json.contains("\"2\":\"two\""));
        Assertions.assertFalse(json.contains("values"));
    }

    @Test
    public void JsonGetterTest() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new A2("John"));
        Assertions.assertTrue(json.contains("\"firstName\":\"John\""));
    }
    @Test
    public void JsonPropertyOrderTest() throws JsonProcessingException{
        String json = mapper.writeValueAsString( new A3("John",23));

        Assertions.assertTrue(json.lastIndexOf("John") > json.lastIndexOf("23"));

    }

    @Test
    void JsonRawValueTest() throws JsonProcessingException {
        String json = mapper.writeValueAsString( new A4("John"));
        Assertions.assertTrue(!json.contains("\"John\""));
    }

    @Test
    void JsonValueTest() throws JsonProcessingException {
        String json = mapper.writeValueAsString( new A5("John",23));
        Assertions.assertTrue(!json.contains("23"));
    }

    @Test
    void JsonRootNameTest() throws JsonProcessingException {
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String json = mapper.writeValueAsString( new A6("John",23));
        Assertions.assertTrue(json.contains("Person"));
    }

    @Test
    void JsonSerializeTest() throws JsonProcessingException {
        String json = mapper.writeValueAsString( new A7("John",LocalDate.of(2018,8,4)));
        Assertions.assertTrue(json.contains("04-08-2018"));
    }
}
