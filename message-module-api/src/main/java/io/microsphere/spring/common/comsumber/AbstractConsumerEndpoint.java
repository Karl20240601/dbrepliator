package io.microsphere.spring.common.comsumber;


import org.springframework.context.SmartLifecycle;

public abstract class AbstractConsumerEndpoint implements ConsumerEndpoint, SmartLifecycle {
    private boolean running;

    @Override
    public boolean isRunning() {
        return false;
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }
}
