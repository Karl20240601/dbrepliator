package io.microsphere.spring.common.binds;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

public interface MessageTargetFactory {
    /**
     * 接收
     * @param inputName
     * @return
     */
    SubscribableChannel createInput(String inputName, String group, MessageHandler...messageHandlers);

    /**
     * 发送
     * @param inputName
     * @return
     */
    MessageChannel createOutput(String inputName);
}
