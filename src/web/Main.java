package web;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws IOException {
        int port = 10000;
        InetSocketAddress local = new InetSocketAddress("localhost", port);
        int connectionBacklog = 100;
        HttpServer localhostServer = HttpServer.create(local, connectionBacklog);
        CspHost host = new CspHost("http://", local.getHostName(), local.getPort());
        List<String> frameDomains = Arrays.asList();
        List<String> appSubdomains = Arrays.asList("sandbox");
        StaticHandler handler = new FileHandler(host, Collections.emptyList(), frameDomains, appSubdomains, Paths.get("assets"), true, true);

        List<String> allowedHosts = Arrays.asList("127.0.0.1:" + local.getPort(), host.host());
        SubdomainHandler subdomainHandler = new SubdomainHandler(allowedHosts, handler, true);
        localhostServer.createContext("/", subdomainHandler);

        localhostServer.setExecutor(Executors.newFixedThreadPool(10));
        localhostServer.start();
        System.out.println("Cacheless Server listening on http://localhost:" + port);
    }
}
