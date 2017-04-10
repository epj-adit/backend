package ch.hsr.adit.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class GenericGsonDeserializer implements JsonDeserializer<Map<String, Object>> {

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    return (Map<String, Object>) recursiveRead(json);
  }

  public Object recursiveRead(JsonElement in) {
    if (in.isJsonObject()) {
      Map<String, Object> map = new HashMap<String, Object>();
      JsonObject jsonObject = in.getAsJsonObject();
      Set<Map.Entry<String, JsonElement>> entitySet = jsonObject.entrySet();
      for (Map.Entry<String, JsonElement> entry : entitySet) {
        map.put(entry.getKey(), recursiveRead(entry.getValue()));
      }
      return map;
    } else if (in.isJsonPrimitive()) {
      JsonPrimitive primitive = in.getAsJsonPrimitive();
      if (primitive.isBoolean()) {
        return primitive.getAsBoolean();
      } else if (primitive.isString()) {
        return primitive.getAsString();
      } else if (primitive.isNumber()) {
        Number number = primitive.getAsNumber();
        if (Math.abs(number.doubleValue()) - number.longValue() < 1e-6) {
          return number.longValue();
        } else {
          return number.doubleValue();
        }
      }
    }
    return null;
  }
}
