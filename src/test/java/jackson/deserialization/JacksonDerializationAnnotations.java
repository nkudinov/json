package jackson.deserialization;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
class MyDateDeserializer extends StdDeserializer<LocalDate> {
    public MyDateDeserializer() {
        this(null);
    }
    public MyDateDeserializer(Class<?> vc) {
        super(vc);
    }

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-DD");
    @Override
    public LocalDate deserialize(
            JsonParser jsonparser, DeserializationContext context)
            throws IOException {
        String text = jsonparser.getText();
         return LocalDate.parse(text,dateTimeFormatter);

    }
}
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JacksonDerializationAnnotations {
 ObjectMapper mapper;
 static class A1{
     private final String name1;
     private final String name2;
     @JsonCreator
     public A1(@JsonProperty("firstName") String name1,@JsonProperty("lastName") String name2) {
         this.name1 = name1;
         this.name2 = name2;
     }

     public String getName1() {
         return name1;
     }

     public String getName2() {
         return name2;
     }

 }
 static class A2{
     private final String name;

     private  int age;
     @JsonCreator
     public A2(@JsonProperty("name") String name, @JacksonInject int age) {
         this.name = name;
         this.age  = age;
     }

     public int getAge() {
         return age;
     }

     public String getName() {
         return name;
     }

 }
 static class A3 {
    private  String name;
    private final Map<String,String> values;

     public A3() {
         this.values = new HashMap<String,String>();
     }
//     public A3(String name ) {
//         this.name = name;
//         this.values = new HashMap<>();
//     }

     public String getName() {
         return name;
     }

     public Map<String, String> getValues() {
         return values;
     }
     @JsonAnySetter
     public void setValue(String key, String value){
         this.values.put(key,value);
     }

     public void setName(String name) {
         this.name = name;
     }
 }
    static class  A4{
        private  String name;
        public A4() {

        }
        @JsonSetter("firstName")
        public void setName(String name) {
            this.name = name;
        }
    }
    static class A5{
      @JsonDeserialize(using = MyDateDeserializer.class)
      public LocalDate date;

    }
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }
    @Test
    void JsonCreatorTest() throws Exception {
        String json = "{\"firstName\":\"John\",\"lastName\":\"Smith\"}";
        A1 a1  = mapper.readValue(json,A1.class);
        Assertions.assertEquals(a1.getName1(),"John");
        Assertions.assertEquals(a1.getName2(),"Smith");
    }

    @Test
    void JacksonInjectText() throws IOException {

        InjectableValues inject = new InjectableValues.Std()
                .addValue(int.class, 23);

        String json = "{\"name\":\"John\"}";
        A2 a2  = mapper
                .reader(inject)
                .forType(A2.class)
                .readValue(json);

        Assertions.assertEquals(a2.getName(),"John");
        Assertions.assertEquals(a2.age,23);
    }

    @Test
    void JsonAnySettetTest()throws Exception {
        String json = "{\"firstName\":\"John\",\"lastName\":\"Smith\"}";
        A3 a3  = mapper.readerFor(A3.class).readValue(json);
        System.out.println(a3.name);
        Assertions.assertNull(a3.getName());
        Assertions.assertTrue(a3.values.containsKey("firstName"));
        Assertions.assertTrue(a3.values.containsValue("John"));

    }

    @Test
    void JsonSetterTest() throws Exception {
      String json = "{\"firstName\":\"John\"}";
      A4 a4 = mapper.readerFor(A4.class).readValue(json);
      Assertions.assertEquals("John",a4.name);
    }

    @Test
    void JsonDeserializeTest() throws IOException {
     String json = "{\"date\":\"2018-01-01\"}";
     A5 a5 = mapper.readerFor(A5.class).readValue(json);
     Assertions.assertEquals(a5.date,LocalDate.of(2018,01,01));
    }
}
