package ch.hsr.adit.domain.model.serializer;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.model.User;

public class UserSerializer implements JsonSerializer<User> {

  @Override
  public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject root = new JsonObject();
    root.addProperty("id", src.getId());
    root.addProperty("username", src.getUsername());
    root.addProperty("email", src.getEmail());
    root.addProperty("isActive", src.isIsActive());
    root.addProperty("isPrivate", src.isIsPrivate());
    root.addProperty("wantsNotification", src.isWantsNotification());
    root.addProperty("jwtToken", src.getJwtToken());
    
    JsonElement serializedCreated = context.serialize(src.getCreated(), Date.class);
    root.add("created", serializedCreated);
   
    JsonElement serializedUpdated = context.serialize(src.getUpdated(), Date.class);
    root.add("updated", serializedUpdated);

    JsonElement serializedRole = context.serialize(src.getRole(), Role.class);
    root.add("role", serializedRole);
    
    return root;
  }
}
