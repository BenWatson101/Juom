package JUOM.WebServices.PerformanceMonitor;

import JUOM.WebServices.Services;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MonitoredThread extends Thread {

    private static final CopyOnWriteArrayList<MonitoredThread> instances = new CopyOnWriteArrayList<>();

    public MonitoredThread(Runnable task) {
        super(task);
        instances.add(this);
    }



    private long getThreadAllocatedBytes() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        if (threadMXBean instanceof com.sun.management.ThreadMXBean) {
            return ((com.sun.management.ThreadMXBean) threadMXBean).getThreadAllocatedBytes(this.getId());
        } else {
            throw new UnsupportedOperationException("ThreadMXBean does not support getThreadAllocatedBytes");
        }
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

    public void printInstancesAndMemory() {
        System.out.println("Total threads: " + getTotalThreads());
        System.out.println("Total threads instances: " + getTotalInstances());
        System.out.println("Total allocated bytes: " + getTotalAllocatedBytes());
        System.out.println("Total memory bytes: " + getTotalMemory());
        System.out.println("Free memory bytes: " + getFreeMemory());
        System.out.print("\n");
    }

    @Override
    public void run() {
        try {
            super.run();
        } finally {
            instances.remove(this);
        }
    }


}