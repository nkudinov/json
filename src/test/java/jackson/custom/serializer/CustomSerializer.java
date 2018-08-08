package jackson.custom.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class User {
  public int age;
  public String name;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
class Item {
  public int id;
  public String name;
  public User owner;

    public Item(int id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }
}
class ItemSerializer extends StdSerializer<Item>{
    public ItemSerializer() {
        this(null);
    }

    public ItemSerializer(Class<Item> t) {
        super(t);
    }

    @Override
    public void serialize(
            Item value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        jgen.writeNumberField("id", value.id);
        jgen.writeStringField("name", value.name);
     //   jgen.writeStartObject(User.class);
        jgen.writeStringField("owner_name", value.owner.name);
        jgen.writeNumberField("owner_age", value.owner.age);
      //  jgen.writeEndObject();
        jgen.writeEndObject();
    }
}
public class CustomSerializer {
    ObjectMapper mapper;
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }
    @Test
    void customSerializerTest() throws JsonProcessingException {
        User user = new User(23,"John");
        Item item = new Item(1,"computer",user);

        SimpleModule module = new SimpleModule();
        module.addSerializer(Item.class, new ItemSerializer());
        mapper.registerModule(module);

        String json = mapper.writeValueAsString(item);
        System.out.println(json);
    }
}
