package JUOM.WebServices;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public final class ClassManager implements Services {

    //moved from Service because it is an interface
    static final List<Services> services = new ArrayList<>();

    public static <T> ArrayList<Class<? extends T>> getAllClasses(Class<T> type) {
        Reflections reflections = new Reflections("");
        return new ArrayList<>(reflections.getSubTypesOf(type));
    }

    @Override
    public void start() throws Exception {}

    @Override
    public void stop() {}

    @Override
    public void execute() {}
}
