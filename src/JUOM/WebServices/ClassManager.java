package JUOM.WebServices;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class ClassManager {

    //moved from Service because it is an interface
    public static final List<Services> services = new ArrayList<>();

    public static <T> ArrayList<Class<? extends T>> getAllClasses(Class<T> type) {
        Reflections reflections = new Reflections("");
        return new ArrayList<>(reflections.getSubTypesOf(type));
    }

}
