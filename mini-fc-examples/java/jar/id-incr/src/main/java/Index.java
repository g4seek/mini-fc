import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.g4seek.minifc.example.idincr.handler.IndexHandler;

public class Index {

    public static void main(String[] args) {
        String json = "{\"key\":\"test\"}";
        JsonParser parser = new JsonParser();
        JsonObject input = parser.parse(json).getAsJsonObject();
        Index index = new Index();
        JsonObject output = index.handle(input);
        System.out.println(output.toString());
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
