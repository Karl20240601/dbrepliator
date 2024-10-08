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



import io.microsphere.spring.db.serialize.api.ObjectInput;
import io.microsphere.spring.db.serialize.api.ObjectOutput;
import io.microsphere.spring.db.serialize.api.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Hessian2 serialization implementation, hessian2 is the default serialization protocol for dubbo
 *
 * <pre>
 *     e.g. &lt;dubbo:protocol serialization="hessian2" /&gt;
 * </pre>
 */
public class Hessian2Serialization implements Serialization {

    @Override
    public ObjectOutput serialize(OutputStream out) throws IOException {
        return new Hessian2ObjectOutput(out);
    }

    @Override
    public ObjectInput deserialize(InputStream is) throws IOException {
        return new Hessian2ObjectInput(is);
    }

}
