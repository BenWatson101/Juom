package JUOM.WebServices;

import java.net.URL;

public class JarChecker implements Services {
    public boolean isRunningFromJar() {
        URL resource = JarChecker.class.getResource("/" + JarChecker.class.getName().replace('.', '/') + ".class");
        return resource != null && resource.getProtocol().equals("jar");
    }

    public boolean isRunningFromJar(Class<?> clazz) {
        URL resource = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class");
        return resource != null && resource.getProtocol().equals("jar");
    }

    @Override
    public void start() throws Exception {}

    @Override
    public void stop() {}

    @Override
    public void execute() {}
}
