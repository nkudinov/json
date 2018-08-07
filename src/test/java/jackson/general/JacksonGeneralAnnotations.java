package jackson.general;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class JacksonGeneralAnnotations {
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }
    static class A1 {
        private  String name;
        public A1() {
        }
        public A1(String name) {
            this.name = name;
        }
        @JsonProperty("firstName")
        public String getName() {
            return name;
        }
        @JsonProperty("fullName")
        public void setName(String name) {
            this.name = name;
        }
    }
    @JsonFormat()
    static class A2 {
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd")
        public LocalDate date;

        public A2(LocalDate date) {
            this.date = date;
        }
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd")
        public LocalDate getDate() {
            return date;
        }
    }
    static class A3{
        public A3(int age, Name name) {
            this.age = age;
            this.internalName = name;
        }

        public int age;
        @JsonUnwrapped
        public Name internalName;
        public static class Name{
            public String firstName;
            public String lastName;

            public Name(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
            }
        }
    }

    static class A4 {

        public int id;

        @JsonView(Views.Public.class)
        public String itemName;

        @JsonView(Views.Internal.class)
        public String ownerName;

        public A4(int id, String itemName, String ownerName) {
            this.id = id;
            this.itemName = itemName;
            this.ownerName = ownerName;
        }
    }
    @JsonFilter("myFilter")
    static class A5{
        public int age;
        public String name;

        public A5(int age, String name) {
            this.age = age;
            this.name = name;
        }
    }

    static class A6{
        public int id;
        public String a6name;

        @JsonManagedReference
        public A7 a7;

        public void setA7(A7 a7) {
            this.a7 = a7;
        }

        public A6(int id, String a6name, A7 a7) {
            this.id = id;
            this.a6name = a6name;
            this.a7 = a7;
        }
    }
    public class A7 {
        public int id;
        public String a7name;

        @JsonBackReference
        public List<A6> a6List;

        public A7(int id, String a7name, List<A6> a6List) {
            this.id = id;
            this.a7name = a7name;
            this.a6List = a6List;
        }
    }
    @Test
    void JsonPropertyTest() throws IOException {
        A1 a1 = new A1("John");
        String json1 = mapper.writeValueAsString(a1);
        Assertions.assertTrue(json1.contains("firstName"));

        String json2 = "{\"fullName\":\"John\"}";
        A1 a2 = mapper.readerFor(A1.class).readValue(json2);
        Assertions.assertEquals("John",a2.getName());
    }
    @Test
    void JsonFormatTest() throws JsonProcessingException {
        A2 a2 = new A2(LocalDate.of(2018,1,1));
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,true);
        String json = mapper.writeValueAsString(a2);
        System.out.println(json);
        //Assertions.assertTrue(json1.contains("firstName"));
    }

    @Test
    void JsonUnwrappedTest() throws JsonProcessingException {
       A3.Name john = new A3.Name("John","Smith");
       A3 a3 = new A3(23,john);
       String json = mapper.writeValueAsString(a3);
        System.out.println(json);
       Assertions.assertFalse(json.contains("internalName"));
    }
    @Test
    void JsonViewTest1() throws JsonProcessingException {
        A4 a4 = new A4(2, "book", "John");
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        String json = mapper.writerWithView(Views.Public.class).writeValueAsString(a4);
        Assertions.assertFalse(json.contains("id"));
        Assertions.assertFalse(json.contains("ownerName"));
    }
    @Test
    void JsonViewTest2() throws JsonProcessingException {
        A4 a4 = new A4(2, "book", "John");

        String json = mapper.writerWithView(Views.Public.class).writeValueAsString(a4);
        System.out.println(json);
        Assertions.assertTrue(json.contains("id"));
        Assertions.assertTrue(json.contains("itemName"));
        Assertions.assertFalse(json.contains("ownerName"));
    }
    @Test
    void JsonViewTest3() throws JsonProcessingException {
        A4 a4 = new A4(2, "book", "John");

        String json = mapper.writerWithView(Views.Internal.class).writeValueAsString(a4);
        Assertions.assertTrue(json.contains("id"));
        Assertions.assertTrue(json.contains("itemName"));
        Assertions.assertTrue(json.contains("ownerName"));
    }
    @Test
    void JsonFilterTest() throws JsonProcessingException {
        A5 a5 = new A5(23,"John");
        FilterProvider filter = new SimpleFilterProvider().addFilter("myFilter",  SimpleBeanPropertyFilter.filterOutAllExcept("age"));
        String json = mapper.writer(filter).writeValueAsString(a5);
        Assertions.assertFalse(json.contains("John"));
    }
    @Test
    void JsonBackReferenceTest() throws JsonProcessingException {
        A6 a6 = new A6(1,"John6",null);
        A7 a7 = new A7(2,"John7", Arrays.asList(a6));
        a6.setA7(a7);
        String json = new ObjectMapper().writeValueAsString(a6);
      //  System.out.println(json);
        Assertions.assertTrue(json.contains("a6name"));
        Assertions.assertTrue(json.contains("a7name"));
    }
}
