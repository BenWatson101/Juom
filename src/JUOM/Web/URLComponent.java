package JUOM.Web;

import JUOM.UniversalObjects.UniversalObject;

import java.util.Hashtable;
import java.util.Map;

public final class URLComponent {

    private final String urlString;
    private final String component;
    private Hashtable<String, Object> parameters = new Hashtable<>();

    private final static String HALF_DELIMITER = "[?]";
    private final static String DELIMITER = "&";
    private final static String EQUALS = "=";
    private boolean onlyComponent = true;

    private int numberOfParameters = 0;

    URLComponent(String component) {
        this.urlString = component;
        String[] parts = component.split(HALF_DELIMITER);

        this.component = parts[0];

        if(parts.length > 1) {
            onlyComponent = false;

            try {
                String[] parameters = parts[0].split(DELIMITER);
                for (String parameter : parameters) {
                    String[] keyValue = parameter.split(EQUALS);
                    if (keyValue.length == 2) {
                        this.parameters.put(keyValue[0], UniversalObject.parse(keyValue[1]));
                        numberOfParameters++;
                    }
                }
            } catch (Exception e) {
                parameters = new Hashtable<>();
                numberOfParameters = 0;
            }
        }
    }

    public String getUrlString() {
        return urlString;
    }

    public Map<String, Object> getParameters() {
        return new Hashtable<>(parameters);
    }

    public boolean isOnlyComponent() {
        return onlyComponent;
    }

    public int getNumberOfParameters() {
        return numberOfParameters;
    }

    public String getComponent() {
        return component;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
