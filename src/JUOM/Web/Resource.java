package JUOM.Web;

import java.util.Dictionary;
import java.util.Hashtable;

public record Resource(String content, String mime) {

    public final static Dictionary<String, String> extensionToMIME = new Hashtable<>();

    static {
        extensionToMIME.put("html", "text/html");
        extensionToMIME.put("css", "text/css");
        extensionToMIME.put("js", "application/javascript");
        extensionToMIME.put("png", "image/png");
        extensionToMIME.put("jpg", "image/jpeg");
        extensionToMIME.put("gif", "image/gif");
        extensionToMIME.put("ico", "image/x-icon");
        extensionToMIME.put("json", "application/json");
        extensionToMIME.put("xml", "application/xml");
        extensionToMIME.put("svg", "image/svg+xml");
        extensionToMIME.put("txt", "text/plain");
        extensionToMIME.put("pdf", "application/pdf");
        extensionToMIME.put("zip", "application/zip");
        extensionToMIME.put("mp4", "video/mp4");
        extensionToMIME.put("mp3", "audio/mpeg");
    }

    public Resource {
        if (content == null) {
            throw new IllegalArgumentException("Resource cannot be null");
        }
        if (mime == null) {
            throw new IllegalArgumentException("Mime cannot be null");
        }
    }
}
