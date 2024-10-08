package com.crazymaker.gateway.core.api.balance;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BalanceStrategyAnnotation {

    String type() default "";


}
