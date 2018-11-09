import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class Server {

    public static void main(String[] args) throws Exception {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), -1);
        server.createContext("/", new Handler());
        server.start();
    }
}
