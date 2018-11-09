import com.google.gson.JsonObject;

public class RandomAge {

    public JsonObject handle(JsonObject jsonObject) {
        JsonObject result = new JsonObject();
        if (!jsonObject.has("name")) {
            result.addProperty("error", "name should be specified");
        } else {
            result.add("name", jsonObject.get("name"));
            result.addProperty("age", 20);
        }
        return result;
    }
}