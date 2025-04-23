package JUOM.Web;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public final class URL {
    Queue<URLComponent> components = new LinkedList<>();
    private URLComponent polled = null;
    private final boolean isFile;
    private final String extension;

    private static final String DELIMITER = "/";
    private static final String EMPTY_COMPONENT = ".";
    private static final String PARENT_COMPONENT = "..";
    private static final String CLEAR_COMPONENT = "...";

    public URL(String url) {
        String[] parts = url.split(DELIMITER);
        isFile = parts[parts.length - 1].contains(".");
        extension = isFile ? parts[parts.length - 1].substring(parts[parts.length - 1].lastIndexOf(".") + 1) : null;


        for (String part : parts) {
            if(!part.isEmpty() && !part.equals(EMPTY_COMPONENT)) {
                components.add(new URLComponent(part));
            }
            if(part.equals(PARENT_COMPONENT)) {
                if(!components.isEmpty()) components.poll();
            }
            if(part.equals(CLEAR_COMPONENT)) {
                components.clear();
            }
        }
    }

    public URLComponent peek() {
        return components.peek();
    }

    public URLComponent next() {
        Iterator<URLComponent> iterator = components.iterator();
        while (iterator.hasNext()) {
            URLComponent current = iterator.next();
            if (current.equals(peek()) && iterator.hasNext()) {
                return iterator.next();
            }
        }
        return null;
    }

    public URL poll() {
        polled = components.poll();
        return this;
    }

    public URL unpoll() {
        if (polled != null) {
            components.add(polled);
            polled = null;
        }
        return this;
    }

    public String path() {
        StringBuilder path = new StringBuilder();
        for (URLComponent component : components) {
            path.append(component.getUrlString()).append(DELIMITER);
        }
        if (!path.isEmpty()) {
            path.deleteCharAt(path.length() - 1); // Remove the last delimiter
        }
        return path.toString();
    }

    public boolean pureComponents() {
        for (URLComponent component : components) {
            if (!component.isOnlyComponent()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }

    public boolean isFile() {
        return isFile;
    }

    public String extension() {
        return extension;
    }

}
