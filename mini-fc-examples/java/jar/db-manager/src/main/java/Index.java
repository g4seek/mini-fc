import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netease.yanxuan.teddy.handler.IndexHandler;

public class Index {

    public static void main(String[] args) {
        String json = "{\"type\":\"INSERT\",\"data\":{\"name\":\"2\",\"code\":\"3\"}}";
        JsonParser parser = new JsonParser();
        JsonObject input = parser.parse(json).getAsJsonObject();
        Index index = new Index();
        index.handle(input);
    }

    public JsonObject handle(JsonObject jsonObject) {
        IndexHandler indexHandler = IndexHandler.getInstance();
        try {
            return indexHandler.handle(jsonObject);
        } catch (Exception e) {
            JsonObject object = new JsonObject();
            e.printStackTrace();
            object.addProperty("error", e.getMessage());
            return object;
        }
    }
}
