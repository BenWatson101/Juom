package JUOM.WebServices.PerformanceMonitor;

import JUOM.WebServices.ILoggers.ILogger;
import JUOM.WebServices.Services;

import static JUOM.WebServices.PerformanceMonitor.MonitoredThread.instances;

public class PerformanceMonitor implements Services {

    protected ILogger console = Services.getService(ILogger.class);

    @Override
    public void start() throws Exception {}

    @Override
    public void stop() {}

    @Override
    public void execute() {
        console.log(InstancesAndMemory());
    }

    public synchronized long getTotalAllocatedBytes() {
        long total = 0;
        synchronized (instances) {
            for (MonitoredThread thread : instances) {
                total += thread.getThreadAllocatedBytes();
            }
        }
        return total;
    }

    public long getTotalInstances() {
        return instances.size();
    }

    public long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    private long getTotalThreads() {
        return Thread.activeCount();
    }

    public String InstancesAndMemory() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Total threads: ").append(getTotalThreads())
                .append("\nTotal threads instances: ").append(getTotalInstances())
                .append("\nTotal allocated bytes: ").append(getTotalAllocatedBytes())
                .append("\nTotal memory bytes: ").append(getTotalMemory())
                .append("\nFree memory bytes: ").append(getFreeMemory())
                .append("\n");
        return sb.toString();
    }
}
