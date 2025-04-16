package JUOM.Web;

import JUOM.UniversalObjects.UniversalException;
import JUOM.UniversalObjects.UniversalObject;

import java.util.Dictionary;
import java.util.Hashtable;

public final class URLComponent {

    private final String component;
    private final Hashtable<String, String> fields = new Hashtable<>();
    private Hashtable<String, UniversalObject> parameters = new Hashtable<>();

    private final static String HALF_DELIMITER = "[?]";
    private final static String DELIMITER = "&";
    private final static String EQUALS = "=";

    private boolean hasFields = false;
    private boolean hasParameters = false;
    private boolean onlyComponent = true;

    private int numberOfFields = 0;
    private int numberOfParameters = 0;

    public  URLComponent(String component) {
        this.component = component;
        String[] parts = component.split(HALF_DELIMITER);
        if(parts.length > 1) {
            onlyComponent = false;
            String[] fields = parts[1].split(DELIMITER);
            for (String field : fields) {
                String[] keyValue = field.split(EQUALS);
                if (keyValue.length == 2) {
                    this.fields.put(keyValue[0], keyValue[1]);
                    hasFields = true;
                    numberOfFields++;
                }
            }
            try {
                String[] parameters = parts[0].split(DELIMITER);
                for (String parameter : parameters) {
                    String[] keyValue = parameter.split(EQUALS);
                    if (keyValue.length == 2) {
                        this.parameters.put(keyValue[0], (UniversalObject) UniversalObject.parse(keyValue[1]));
                        hasParameters = true;
                        numberOfParameters++;
                    }
                }
            } catch (Exception e) {
                parameters = new Hashtable<>();
                hasParameters = false;
                numberOfParameters = 0;
            }
        }
    }

    public String getComponent() {
        return component;
    }

    public Dictionary<String, String> getFields() {
        return new Hashtable<>(fields);
    }

    public Dictionary<String, UniversalObject> getParameters() {
        return new Hashtable<>(parameters);
    }

    public boolean hasFields() {
        return hasFields;
    }

    public boolean hasParameters() {
        return hasParameters;
    }

    public boolean isOnlyComponent() {
        return onlyComponent;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public int getNumberOfParameters() {
        return numberOfParameters;
    }
}
