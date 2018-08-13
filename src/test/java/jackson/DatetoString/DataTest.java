package jackson.DatetoString;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class DataTest {
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }
    static class A{
        public LocalDate date;

        public A(LocalDate date) {
            this.date = date;
        }
    }
    @Test
    void SimpleDateFormatTest() throws JsonProcessingException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(dateFormat);
        A a = new A(LocalDate.of(2018,1,1));
        String json = mapper.writeValueAsString(a);
        System.out.println(json);
    }


}
