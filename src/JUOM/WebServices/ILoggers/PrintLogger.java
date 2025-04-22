package JUOM.WebServices.ILoggers;

public class PrintLogger implements ILogger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }

    @Override
    public void start() throws Exception {
        log(this.getClass().getName() + " logging begin:");
    }

    @Override
    public void stop() {
        log(this.getClass().getName() + " logging end.");
    }

    @Override
    public void execute() {}
}
