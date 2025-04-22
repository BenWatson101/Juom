package JUOM.WebServices;

public interface Services {

    void start() throws Exception;
    void stop();
    void execute();

    public static void addService(Services services) {
        if (services == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        for (Services existingServices : ClassManager.services) {
            if (existingServices.getClass().isAssignableFrom(services.getClass()) ||
                    services.getClass().isAssignableFrom(existingServices.getClass())) {
                throw new IllegalArgumentException("A service of the same class or a child/parent class already exists");
            }
        }
        ClassManager.services.add(services);
    }

    public static <T extends Services> T getService(Class<T> serviceClass) {
        for (Services services : ClassManager.services) {
            if (serviceClass.isAssignableFrom(services.getClass())) {
                return (T) services;
            }
        }
        throw new IllegalArgumentException("No available service uses " + serviceClass.getName());
    }
}
