package com.crazymaker.springcloud.common.distribute.idGenerator;

public interface IdFactory {


    IdGenerator getIdGenerator(String type);

}
