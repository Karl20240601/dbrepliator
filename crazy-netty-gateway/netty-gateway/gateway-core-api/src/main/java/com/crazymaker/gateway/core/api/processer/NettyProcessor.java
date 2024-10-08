package com.crazymaker.gateway.core.api.processer;

import com.crazymaker.gateway.core.api.context.HttpRequestWrapper;
import com.crazymaker.micro.core.servcie.MicroCore;

public interface NettyProcessor extends MicroCore {

    void process(HttpRequestWrapper wrapper);

    void start();

    void shutDown();
}
