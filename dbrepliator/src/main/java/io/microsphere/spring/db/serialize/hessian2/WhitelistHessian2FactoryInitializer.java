/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.microsphere.spring.db.serialize.hessian2;

import com.alibaba.com.caucho.hessian.io.SerializerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;


public class WhitelistHessian2FactoryInitializer extends AbstractHessian2FactoryInitializer implements EnvironmentAware {
    private Environment environment;

    @Override
    public SerializerFactory createSerializerFactory() {
        SerializerFactory serializerFactory = new Hessian2SerializerFactory();
        String whiteList = environment.getProperty(WHITELIST);
        if ("true".equals(whiteList)) {
            serializerFactory.getClassFactory().setWhitelist(true);
            String allowPattern = environment.getProperty(ALLOW);
            if (StringUtils.isNotEmpty(allowPattern)) {
                serializerFactory.getClassFactory().allow(allowPattern);
            }
        } else {
            serializerFactory.getClassFactory().setWhitelist(false);
            String denyPattern = environment.getProperty(DENY);
            if (StringUtils.isNotEmpty(denyPattern)) {
                serializerFactory.getClassFactory().deny(denyPattern);
            }
        }
        return serializerFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment =  environment;
    }
}
