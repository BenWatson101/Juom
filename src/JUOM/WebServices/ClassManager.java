package JUOM.WebServices;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class ClassManager {

    //moved from Service because it is an interface
    public static final List<Service> services = new ArrayList<>();

    public static <T> ArrayList<Class<? extends T>> getAllClasses() {
        Reflections reflections = new Reflections("");
        return (ArrayList<Class<? extends T>>) reflections.getSubTypesOf((Class<T>) Service.class).stream().toList();
    }

}
