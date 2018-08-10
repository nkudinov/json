package jackson.typefactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

class A {
    private final int age;
    private final String name;

    public A(@JsonProperty("name") String name,@JsonProperty("age") int age) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}
//@RunWith(JUnitPlatform.class)
public class TypeFactoryTest {
    ObjectMapper mapper;
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void typeFactorySimpleTest() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(A.class);
        String json ="{\"name\":\"John\",\"age\":\"23\"}";
        A a  = mapper.readerFor(javaType).readValue(json);
        Assertions.assertEquals(a.getAge() ,23);
        Assertions.assertEquals(a.getName(),"John");
    }
    @DisplayName("Test1")
    @Test
    void typeFactoryListTest() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(LinkedList.class,A.class);
        String json ="[{\"name\":\"John\",\"age\":\"23\"}]";
        List<A> a  = mapper.readerFor(javaType).readValue(json);
        Assertions.assertEquals(a.get(0).getAge() ,23);
        Assertions.assertEquals(a.get(0).getName(),"John");
    }
}
