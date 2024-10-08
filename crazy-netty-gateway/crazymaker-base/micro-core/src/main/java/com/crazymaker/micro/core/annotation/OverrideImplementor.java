package com.crazymaker.micro.core.annotation;

import com.crazymaker.micro.core.servcie.MicroCore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OverrideImplementor {
    Class<? extends MicroCore> value();
}
