package JUOM.WebServices.PerformanceMonitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MonitoredThread extends Thread {

    static final CopyOnWriteArrayList<MonitoredThread> instances = new CopyOnWriteArrayList<>();

    public MonitoredThread(Runnable task) {
        super(task);
        instances.add(this);
    }



    long getThreadAllocatedBytes() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        if (threadMXBean instanceof com.sun.management.ThreadMXBean) {
            return ((com.sun.management.ThreadMXBean) threadMXBean).getThreadAllocatedBytes(this.getId());
        } else {
            throw new UnsupportedOperationException("ThreadMXBean does not support getThreadAllocatedBytes");
        }
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