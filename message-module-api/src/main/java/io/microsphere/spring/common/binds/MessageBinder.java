package io.microsphere.spring.common.binds;

/**
 * MessageBinder
 *
 */
public   interface MessageBinder {
    void bindConsumber(String inputName);

    void bindProducer(String outputName);

}
