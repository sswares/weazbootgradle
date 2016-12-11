package net.weaz.auth.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.weaz.auth.security.userdetails.CustomUserDetails;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class CustomUserDetailsJsonComponent {

    public static class Serializer extends JsonSerializer<CustomUserDetails> {

        @Override
        public void serialize(CustomUserDetails userDetails, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("id", userDetails.getId());
            jsonGenerator.writeObjectField("username", userDetails.getUsername());
            jsonGenerator.writeObjectField("firstName", userDetails.getFirstName());
            jsonGenerator.writeObjectField("lastName", userDetails.getLastName());
            jsonGenerator.writeObjectField("favoriteCat", userDetails.getFavoriteCat());
            jsonGenerator.writeObjectField("authorities", userDetails.getAuthorities());
            jsonGenerator.writeEndObject();
        }
    }
}