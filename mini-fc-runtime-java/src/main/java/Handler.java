import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Handler implements HttpHandler {

    private final String MODULE_NAME = "TestApp";

    private final String METHOD_NAME = "handle";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String uuid = UUID.randomUUID().toString();
        InputStream is = httpExchange.getRequestBody();
        JsonParser parser = new JsonParser();
        JsonObject input = new JsonObject();
        JsonObject output = new JsonObject();
        try {
            JsonElement ie = parser.parse(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
            if (ie != null) {
                input = ie.getAsJsonObject();
            }
            printLog(uuid, "INFO", "request:" + input.toString());
            Class clazz = Class.forName(MODULE_NAME);
            Object obj = clazz.newInstance();
            Method method = clazz.getMethod(METHOD_NAME, JsonObject.class);
            output = (JsonObject) method.invoke(obj, input);
        } catch (Exception e) {
            printLog(uuid, "ERROR", "error:" + e.getMessage());
            e.printStackTrace();
            output.addProperty("error", e.getMessage());
            writeResponse(httpExchange, output);
            return;
        }
        String outputStr = "";
        if (output != null) {
            outputStr = output.toString();
        }
        printLog(uuid, "INFO", "response:" + outputStr);
        writeResponse(httpExchange, output);

    }

    private void printLog(String uuid, String level, String content) {
        StringBuilder builder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        builder.append(dtf.format(now));
        builder.append(" [").append(level).append("] ");
        builder.append("traceId=").append(uuid).append(", ");
        builder.append(content);
        System.out.println(builder.toString());
    }

    private void writeResponse(HttpExchange httpExchange, JsonObject output) throws IOException {
        String outputStr = "";
        if (output != null) {
            outputStr = output.toString();
        }
        byte[] bytes = outputStr.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
