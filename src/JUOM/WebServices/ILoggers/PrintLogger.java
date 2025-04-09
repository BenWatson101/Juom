package JUOM.WebServices.ILoggers;

public class PrintLogger implements ILogger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
