package jackson.polymorphictype;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JacksonPolymorphicTypeAnnotations {
    ObjectMapper mapper;
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }
    static class Box {
       public Animal animal;

        public Box(Animal animal) {
            this.animal = animal;
        }
        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.PROPERTY,
                property = "type_of_element")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = Dog.class, name = "DogType"),
                @JsonSubTypes.Type(value = Cat.class, name = "CatType")
        })
        public static class Animal {
         public String name;
            public Animal(String name) {
                this.name = name;
           }
        }
       public static class Dog extends Animal{
          public double barkVolume;

           public Dog(String name,double barkVolume) {
               super(name);
               this.barkVolume = barkVolume;
           }
       }
       public static class Cat extends Animal{
           public Cat(String name,int lives) {
               super(name);
               this.lives = lives;
           }

           public int lives;
       }
    }
    @Test
    void name() throws JsonProcessingException {
        Box.Animal dog = new Box.Dog("Tom",1.1);
        Box box =  new Box(dog);
        String json = mapper.writeValueAsString(box);
        System.out.println(json);
        Assertions.assertTrue(true);
    }
}
