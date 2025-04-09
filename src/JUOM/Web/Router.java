package JUOM.Web;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public abstract class Router extends Page {

    private static final Map<String, ServerObject> ExistingServerObjects = new Hashtable<>();

    private final Map<String, ServerObject> serverObjectMap = new HashMap<>();

    protected void handleRequest(Client c, String url) throws CompleteClientResponse {

        if(!c.getHeader("Method")[0].equals("GET")) {
            return;
        }

        if(url.equals("/")) {
            c.setResponse(startingPage());
            return;
        }

//        System.out.println("Server URL: " + url);
//        System.out.println("Server next: " + nextURLPart(url));

        if(nextURLPart(url).equals(getClass().getSimpleName())) {
            url = truncateUrL(url);
        }

        ServerObject obj = serverObjectMap.get(nextURLPart(url));

        if(obj != null) {
            obj.handleURL(c, truncateUrL(url));

        } else {
            super.handleURL(c, url);
        }
    }

    protected final Router addServerObject(ServerObject obj) {
        if(ExistingServerObjects.containsKey(obj.getClass().getSimpleName())) {
            throw new IllegalArgumentException("Server object already exists");
        }
        serverObjectMap.put(obj.getClass().getSimpleName(), obj);
        ExistingServerObjects.put(obj.getClass().getSimpleName(), obj);
        obj.parent = this;
        return this;
    }
}
