package io.microsphere.spring.common.comsumber;

import org.springframework.messaging.SubscribableChannel;
import java.util.Map;

public interface ConsumerEndpoint  {
    void  setInputMessageChannel(SubscribableChannel subscribableChannel);
}
