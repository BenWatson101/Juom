package JUOM.Web;

import JUOM.JHTML.JHTML;
import JUOM.UniversalObjects.UniversalObject;
import JUOM.UniversalObjects.WebMethod;
import JUOM.WebServices.FileManager;
import JUOM.WebServices.ILoggers.ILogger;
import JUOM.WebServices.Services;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static JUOM.Web.Resource.*;

public abstract class ServerObject extends UniversalObject {

    ServerObject parent = null;

    protected final ILogger console = Services.getService(ILogger.class);
    protected final FileManager fileManager = Services.getService(FileManager.class);

    private static boolean paramsMatch(Map<String, Object> dict, Parameter[] params) {
        if(dict.size() != params.length) {
            return false;
        }

        for (Parameter param : params) {
            if (!dict.containsKey(param.getName())) {
                return false;
            }
        }

        return true;
    }

    protected final UniversalObject executeMethod(String methodName, Map<String, Object> params) throws IOException {

        if(methodName.equals(this.getClass().getName())) {
            Constructor<?>[] constructors = this.getClass().getConstructors();
            for (Constructor<?> constructor : constructors) {
                if(paramsMatch(params, constructor.getParameters())) {
                    try {
                        constructor.setAccessible(true);
                        return UniversalObject.convert(constructor.newInstance(params.values().toArray()));
                    } catch (Exception ignored) {
                        break;
                    }
                }
            }
        } else {
            for (Method method : this.getClass().getMethods()) {
                if (method.getName().equals(methodName)
                        && paramsMatch(params, method.getParameters())
                        && method.isAnnotationPresent(WebMethod.class)) {

                    try {
                        method.setAccessible(true);
                        return UniversalObject.convert(method.invoke(this, params.values().toArray()));
                    } catch (Exception ignored) {
                        break;
                    }
                }
            }
        }
        throw new IOException("Method not found");
    }




    protected final Resource handleResource(URL path) throws IOException {

        if(path.isFile()) {
            byte[] bytes = fileManager.readFile(path.path(), this.getClass());
            if (bytes == null) {
                return new Resource("Failed to load content", extensionToMIME.get("txt"));
            }
            return new Resource(new String(bytes, StandardCharsets.UTF_8), extensionToMIME.get(path.extension()));
        } else {
            throw new IOException("Invalid path");
        }
    }


    protected abstract JHTML objectOrResourceNotFound(String message);

    protected void handleURL(Client c, URL url) throws CompleteClientResponse {

        try {
            c.setResponse(handleResource(url)).completeResponse();
        } catch (IOException ignored) {}

        try {
            URLComponent next = url.next();
            if(next == null) {
                c.setResponse(executeMethod(next.getComponent(), url.peek().getParameters())).completeResponse();
            }
        } catch (IOException ignored) {}
    }

    protected String path() {
        return this.parent.path() + this.getClass().getSimpleName()  + "/";
    }

    protected final void build() {

    }
}
