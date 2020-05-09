package org.springframework.beanz.context;

public interface ApplicationListener<E> {
    void onApplicationEvent(E event);
}
