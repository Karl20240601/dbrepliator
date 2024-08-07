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

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import io.microsphere.spring.db.serialize.api.ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;

import  io.microsphere.spring.db.serialize.hessian2.AbstractHessian2FactoryInitializer;

/**
 * Hessian2 object output implementation
 */
public class Hessian2ObjectOutput implements ObjectOutput {

    private static ThreadLocal<Hessian2Output> INPUT_TL = ThreadLocal.withInitial(() -> {
        Hessian2Output h2i = new Hessian2Output(null);
        h2i.setSerializerFactory(AbstractHessian2FactoryInitializer.getSerializerFactory1());
        h2i.setCloseStreamOnClose(true);
        return h2i;
    });
    private final Hessian2Output mH2o;

    public Hessian2ObjectOutput(OutputStream os) {
        mH2o = INPUT_TL.get();
        mH2o.init(os);
    }

    @Override
    public void writeBool(boolean v) throws IOException {
        mH2o.writeBoolean(v);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        mH2o.writeInt(v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        mH2o.writeInt(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        mH2o.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        mH2o.writeLong(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        mH2o.writeDouble(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        mH2o.writeDouble(v);
    }

    @Override
    public void writeBytes(byte[] b) throws IOException {
        mH2o.writeBytes(b);
    }

    @Override
    public void writeBytes(byte[] b, int off, int len) throws IOException {
        mH2o.writeBytes(b, off, len);
    }

    @Override
    public void writeUTF(String v) throws IOException {
        mH2o.writeString(v);
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        mH2o.writeObject(obj);
    }

    @Override
    public void flushBuffer() throws IOException {
        mH2o.flushBuffer();
    }

    public OutputStream getOutputStream() throws IOException {
        return mH2o.getBytesOutputStream();
    }
}
