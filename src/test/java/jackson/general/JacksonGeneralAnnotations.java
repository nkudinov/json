package jackson.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

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
}
