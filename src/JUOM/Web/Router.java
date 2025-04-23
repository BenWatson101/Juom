package JUOM.Web;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public abstract class Router extends Page {

    private static final Map<String, ServerObject> ExistingServerObjects = new Hashtable<>();

    private final Map<String, ServerObject> serverObjectMap = new HashMap<>();

    protected void handleRequest(Client c, URL url) throws CompleteClientResponse {

        if(url.path().isEmpty()) {
            c.setResponse(startingPage()).completeResponse();
        }

        if(Objects.requireNonNull(url.next()).getUrlString().equals(getClass().getSimpleName())) {
            url.poll();
        }

        ServerObject obj = serverObjectMap.get(Objects.requireNonNull(url.next()).getUrlString());

        if(obj != null) {
            obj.handleURL(c, url.poll());
        } else {
            super.handleURL(c, url);
        }
    }

    protected final Router addServerObject(ServerObject obj) {
        if(ExistingServerObjects.containsKey(obj.getClass().getName())) {
            throw new IllegalArgumentException("Server object already exists");
        }
        serverObjectMap.put(obj.getClass().getSimpleName(), obj);
        ExistingServerObjects.put(obj.getClass().getName(), obj);
        obj.parent = this;
        return this;
    }

    protected final Router addServerObjects(ServerObject[] obj) {
        for (ServerObject serverObject : obj) {
            addServerObject(serverObject);
        }
        return this;
    }
}
