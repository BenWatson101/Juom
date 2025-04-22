package JUOM.Web;

import JUOM.WebServices.ILoggers.ILogger;
import JUOM.WebServices.ILoggers.PrintLogger;
import JUOM.WebServices.PerformanceMonitor.MonitoredThread;
import JUOM.WebServices.PerformanceMonitor.PerformanceMonitor;
import JUOM.WebServices.Services;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class HTTPServer extends Router {

    private final int port;
    private final ServerSocket serverSocket;
    private boolean running = true;

    private String domainName;

    public HTTPServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.domainName = "http://localhost:" + port;
    }

    public final HTTPServer start() {
        running = true;


        new MonitoredThread(() -> {
            while (running) {
                try (Socket clientSocket = serverSocket.accept()){
                    new MonitoredThread(() -> {
                        try (Client c = new Client(clientSocket)) {
                            try { handleRequest(c, processUrl(c.getURL())); } catch (CompleteClientResponse ignored) {}
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new MonitoredThread(() -> {
            while (running) {
                try {
                    Thread.sleep(5000);
                    Services.getService(ILogger.class).log(Services.getService(PerformanceMonitor.class).InstancesAndMemory());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return this;
    }

    public void stop() {
        running = false;
    }

    //domain name WITHOUT the slash at the end
    @Override
    protected String path() {
        return domainName + "/";
    }

    protected final HTTPServer setDomainName(String domainName) {
        this.domainName = domainName;
        return this;
    }


}
