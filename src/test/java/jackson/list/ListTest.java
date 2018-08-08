package jackson.list;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scala.collection.mutable.LinkedHashMap;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


class A {
    public String name;
    public int age;
    public A(@JsonProperty("name") String name, @JsonProperty("age") int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof A)) return false;
        A a = (A) o;
        return age == a.age &&
                Objects.equals(name, a.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, age);
    }
}
public class ListTest {
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }


    @Test
    void defaultDesirializationTest() throws IOException {
        String json = "[{\"name\":\"John\",\"age\":23},{\"name\":\"Ivan\",\"age\":23}]";
        List list =  mapper.readValue(json,List.class);

    }
    @Test
    void typeReferenceTest() throws IOException {

        String json = "[{\"name\":\"John\",\"age\":23},{\"name\":\"Ivan\",\"age\":23}]";
        List<A> list =  mapper.readValue(json,new TypeReference<List<A>>(){});
        Assertions.assertTrue(list.indexOf(new A("John",23)) >=0);
        Assertions.assertTrue(list.indexOf(new A("Ivan",23)) >=0);

    }
    @Test
    void constructCollectionTypeTest() throws IOException {
        String json = "[{\"name\":\"John\",\"age\":23},{\"name\":\"Ivan\",\"age\":23}]";

        CollectionType javaType = mapper.getTypeFactory()
                .constructCollectionType(List.class, A.class);

        List<A> list =  mapper.readValue(json,javaType);
        Assertions.assertTrue(list.indexOf(new A("John",23)) >=0);
        Assertions.assertTrue(list.indexOf(new A("Ivan",23)) >=0);
    }
}
