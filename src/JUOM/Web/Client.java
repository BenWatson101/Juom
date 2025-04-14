package JUOM.Web;

import JUOM.JHTML.JHTML;
import JUOM.UniversalObjects.UniversalException;
import JUOM.UniversalObjects.UniversalObject;

import java.io.*;
import java.net.Socket;
import java.util.*;

public final class Client implements AutoCloseable {
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Socket socket;
    private final Dictionary<String, String[]> headers = new Hashtable<>();
    private final Map<String, LinkedList<String>> responseHeaders = new Hashtable<>();

    private int responseCode = 200;
    private String responseMessage = "OK";
    private String content = "";

    private String body = "";


    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String line;

        boolean bodySection = false;

        if(!(line = in.readLine()).isEmpty()) {
            String[] parts = line.split(" ");
            if (parts.length == 3) {
                headers.put("Method", new String[] {parts[0]});
                headers.put("URL", new String[] {parts[1]});
                headers.put("Version", new String[] {parts[2]});
            }
        }

        while (!(line = in.readLine()).isEmpty()) {
            if (line.trim().isEmpty()) {
                bodySection = true;
                continue;
            }
            if(!bodySection) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String headerName = parts[0].trim();
                    String headerValue = parts[1].trim();
                    // Special handling for headers that shouldn't be split
                    if (!headerName.equalsIgnoreCase("Set-Cookie") &&
                            !headerName.equalsIgnoreCase("Cookie")) {
                        headers.put(headerName, headerValue.split(","));
                    } else {
                        headers.put(headerName, new String[]{headerValue});
                    }
                }
            } else {
                body = line + "\n";
            }

        }
    }

    private void respond() throws IOException {
        out.write("HTTP/1.1 " + responseCode + " " + responseMessage + "\r\n");

        for (String header : responseHeaders.keySet()) {
            out.write(header + ": " + String.join(", ", responseHeaders.get(header)) + "\r\n");
        }

        out.write("\r\n");
        out.write(content);

        out.flush();
    }


    @Override
    public void close() throws IOException {

        respond();

        in.close();
        out.close();
        socket.close();
    }

    public String[] getHeader(String header) {
        return headers.get(header);
    }

    public String getURL() {
        return headers.get("URL")[0];
    }

    public String getMethod() {
        return headers.get("Method")[0];
    }

    public String getBody() {
        return body;
    }

    public Client addResponseHeader(String header, String value) {
        LinkedList<String> values = responseHeaders.computeIfAbsent(header, k -> new LinkedList<>());
        values.add(value);
        return this;
    }

    public Client setResponseHeaders(String header, String value) {
        LinkedList<String> values = new LinkedList<>();
        values.add(value);
        responseHeaders.put(header, values);
        return this;
    }

    public Client setResponseCode(int code) {
        responseCode = code;
        return this;
    }

    public Client setResponseMessage(String message) {
        responseMessage = message;
        return this;
    }



    public Client setContent(String content) {
        this.content = content;
        return this;
    }

    public Hashtable<String, String> getCookies() {
        Hashtable<String, String> cookies = new Hashtable<>();
        String[] cookieHeaders = headers.get("Cookie");
        if (cookieHeaders != null) {
            String[] cookieParts = cookieHeaders[0].split(";");
            for (String cookiePart : cookieParts) {
                String[] cookie = cookiePart.split("=", 2);
                if (cookie.length == 2) {
                    cookies.put(cookie[0].trim(), cookie[1].trim());
                }
            }
        }
        return cookies;
    }

    public Client setCookie(String name, String value) {
        String setCookieHeader = responseHeaders.get("Set-Cookie") == null ? "" : responseHeaders.get("Set-Cookie").get(0);
        setCookieHeader += name + "=" + value + "; ";
        responseHeaders.put("Set-Cookie", new LinkedList<>(List.of(setCookieHeader)));
        return this;
    }

    public Client addCookieType(String type) {
        responseHeaders.compute("Set-Cookie", (key, values) -> {
            if (values == null || values.isEmpty()) {
                values = new LinkedList<>();
                values.add(type + ";");
            } else {
                String currentHeader = values.get(0);
                if (!currentHeader.contains(type + ";")) {
                    values.set(0, currentHeader + type + "; ");
                }
            }
            return values;
        });
        return this;
    }

    public Client setResponse(JHTML jhtml) {
        setResponseHeaders("Content-Type", "text/html");
        setContent(jhtml.html());
        return this;
    }

    public Client setResponse(UniversalObject obj) {
        setResponseHeaders("Content-Type", "application/json");
        setContent(obj.json());
        return this;
    }

    public Client setResponse() {
        setResponseHeaders("Content-Type", "text/plain");
        setContent("");
        return this;
    }

    public Client setResponse(String content) {
        setResponseHeaders("Content-Type", "text/plain");
        setContent(content);
        return this;
    }

    public void setResponse(UniversalException e) {
        setResponse((UniversalObject) e);
    }

    public Client setResponse(byte[] content) {
        setResponseHeaders("Content-Type", "application/octet-stream");
        setContent(new String(content));
        return this;
    }

    public Client setResponse(Resource resource) {
        setResponseHeaders("Content-Type", resource.mime());
        setContent(resource.content());
        return this;
    }

    public void completeResponse() throws CompleteClientResponse {
        throw new CompleteClientResponse();
    }

}
