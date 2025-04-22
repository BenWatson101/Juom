package JUOM.Web;

import JUOM.UniversalObjects.UniversalObject;

import java.util.Dictionary;
import java.util.Hashtable;

public final class URLComponent {

    private final String component;
    private final String page;
    private Hashtable<String, UniversalObject> parameters = new Hashtable<>();

    private final static String HALF_DELIMITER = "[?]";
    private final static String DELIMITER = "&";
    private final static String EQUALS = "=";
    private boolean onlyPage = true;

    private int numberOfParameters = 0;

    public URLComponent(String component) {
        this.component = component;
        String[] parts = component.split(HALF_DELIMITER);

        page = parts[0];

        if(parts.length > 1) {
            onlyPage = false;

            try {
                String[] parameters = parts[0].split(DELIMITER);
                for (String parameter : parameters) {
                    String[] keyValue = parameter.split(EQUALS);
                    if (keyValue.length == 2) {
                        this.parameters.put(keyValue[0], (UniversalObject) UniversalObject.parse(keyValue[1]));
                        numberOfParameters++;
                    }
                }
            } catch (Exception e) {
                parameters = new Hashtable<>();
                numberOfParameters = 0;
            }
        }
    }

    public String getComponent() {
        return component;
    }

    public Dictionary<String, UniversalObject> getParameters() {
        return new Hashtable<>(parameters);
    }

    public boolean isOnlyPage() {
        return onlyPage;
    }

    public int getNumberOfParameters() {
        return numberOfParameters;
    }

    public String getPage() {
        return page;
    }
}
