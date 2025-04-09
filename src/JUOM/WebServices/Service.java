package JUOM.WebServices;

public interface Service {

    void start() throws Exception;
    void stop();

    static void addService(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        for (Service existingService : ClassManager.services) {
            if (existingService.getClass().isAssignableFrom(service.getClass()) ||
                    service.getClass().isAssignableFrom(existingService.getClass())) {
                throw new IllegalArgumentException("A service of the same class or a child class already exists");
            }
        }
        ClassManager.services.add(service);
    }

    static <T> T getService(Class<T> serviceClass) {
        for (Service service : ClassManager.services) {
            if (serviceClass.isAssignableFrom(service.getClass())) {
                return (T) service;
            }
        }
        throw new IllegalArgumentException("No available service uses " + serviceClass.getName());
    }

}
