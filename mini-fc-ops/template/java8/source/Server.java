import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class Server_${RANDOM} {

    public static void main(String[] args) throws Exception {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), -1);
        server.createContext("/", new Handler_${RANDOM}());
        server.start();
    }
}
