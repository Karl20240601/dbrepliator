package io.microsphere.dynamic.jdbc.spring.boot.namespaces;

import io.microsphere.dynamic.jdbc.spring.boot.namespaces.property.NamespaceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public class NameSpaceConfigFileApplicationListener implements EnvironmentPostProcessor {

    private static final String NAMESPANCE_ENABLE_PROPERTY = "spring.applicationcontext.namespace.enable";
    public static final String NAMESPANCE_PREFIX = "spring.application.namespaces";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        boolean property = environment.getProperty(NAMESPANCE_ENABLE_PROPERTY, boolean.class, false);
        if (property) {
            return;
        }
        BindResult<NamespaceProperties> bind = Binder.get(environment).bind(NAMESPANCE_PREFIX, Bindable.ofInstance(new NamespaceProperties()));
        NamespaceProperties namespaceProperties = bind.get();
    }
}
