package JUOM.Web;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class URL {
    Queue<URLComponent> components = new LinkedList<>();

    public URL(String url) {
        String[] parts = url.split("/");

        for (String part : parts) {
            if(!part.isEmpty()) components.add(new URLComponent(part));
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

    public URL pop() {
        components.poll();
        return this;
    }

}
