package org.springframework.beanz.context;

public class CustomService implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println(">> ContextClosed EVENT");
    }
}
