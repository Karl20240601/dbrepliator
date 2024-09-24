package io.microsphere.dynamic.jdbc.spring.boot.namespaces.runnber;

import io.microsphere.dynamic.jdbc.spring.boot.namespaces.NamespaceBeanNameGenerator;
import io.microsphere.dynamic.jdbc.spring.boot.namespaces.property.NamespaceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static io.microsphere.spring.util.ResourceLoaderUtils.getResourcePatternResolver;

public class DefaultNamespaceRunner implements NamespaceRunner {
    private static final String DEFAULT_SEARCH_LOCATIONS = "classpath:/";
    private static final String DEFAULT_NAMES = "application";
    private static final String[] SUPPORT_FILENAME_EXTENTION = {"properties","yml"};


    private ConfigurableEnvironment environment;
    private final BeanNameGenerator beanNameGenerator;
    private final NamespaceProperty property;
    private ConfigurableApplicationContext parentApplicationContext;
    private ConfigurableEnvironment parentEnvironment;
    private final ResourceLoader resourceLoader;
    private static final Logger logger = LoggerFactory.getLogger(DefaultNamespaceRunner.class);

    public DefaultNamespaceRunner(NamespaceProperty property, ConfigurableEnvironment parentEnvironment, ConfigurableApplicationContext parentApplicationContext, ResourceLoader resourceLoader) {
        this.parentEnvironment = parentEnvironment;
        this.parentApplicationContext = parentApplicationContext;
        this.property = property;
        this.resourceLoader = resourceLoader;
        this.beanNameGenerator = new NamespaceBeanNameGenerator(property.getNameSpaceName());
        this.environment = prepareConfigurableEnvironment();
    }


    private ConfigurableEnvironment prepareConfigurableEnvironment() {
        ConfigurableEnvironment orcreateEnvirment = getOrcreateEnvirment();
        return orcreateEnvirment;
    }


    private ConfigurableEnvironment getOrcreateEnvirment() {
        if (environment != null) {
            return environment;
        }
        StandardEnvironment standardEnvironment = new StandardEnvironment();
        List<PropertySourceLoader> propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, parentApplicationContext.getClassLoader());
        String pathPattern = getPathPattern(property);
        Collection<String> files = getFiles(pathPattern);
        loadDefaultProperties(files, propertySourceLoaders, this.resourceLoader, standardEnvironment);
        return standardEnvironment;
    }

    private Collection<String> getFiles(String pathPattern) {
        try {
            Enumeration<URL> resources = parentApplicationContext.getClassLoader().getResources(pathPattern);
            List<String> objects = Collections.emptyList();
            while (resources.hasMoreElements()) {
                String file = resources.nextElement().getFile();
                String fileName = file.substring(file.lastIndexOf(File.pathSeparator) + 1);
                if (fileName.startsWith(String.format("%s-%s", DEFAULT_NAMES, this.property.getNameSpaceName()))) {
                    objects.add(file);
                }
            }
            return objects;
        } catch (IOException e) {
            logger.error("");
        }
        return Collections.emptyList();
    }

    private String getPathPattern(NamespaceProperty namespaceProperty) {
        String springProfieActive = namespaceProperty.getSpringProfieActive();
        //classpath:/{namsespace}/
        return String.format("%s%s/", DEFAULT_SEARCH_LOCATIONS, namespaceProperty.getNameSpaceName());

    }

    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void run() {

    }


    private void loadDefaultProperties(Collection<String> defaultPropertiesResources,
                                       List<PropertySourceLoader> propertySourceLoaders,
                                       ResourceLoader resourceLoader,
                                       ConfigurableEnvironment configurableEnvironment) {
        logger.debug("Start loading the 'defaultProperties' resource path list: {}", defaultPropertiesResources);
        ResourcePatternResolver resourcePatternResolver = getResourcePatternResolver(resourceLoader);
        for (String defaultPropertiesResource : defaultPropertiesResources) {
            try {
                for (Resource resource : resourcePatternResolver.getResources(defaultPropertiesResource)) {
                    if (!loadDefaultProperties(defaultPropertiesResource, resource, propertySourceLoaders, configurableEnvironment)) {
                        logger.warn("'defaultProperties' resource [location: {}] failed to load, please confirm the resource can be processed!", resource.getURL());
                    }
                }
            } catch (IOException e) {
                logger.warn("'defaultProperties' resource [location: {}] does not exist, please make sure the resource is correct!", defaultPropertiesResource, e);
            }
        }
    }

    private boolean loadDefaultProperties(String defaultPropertiesResource, Resource resource,
                                          List<PropertySourceLoader> propertySourceLoaders,
                                          ConfigurableEnvironment configurableEnvironment) throws IOException {
        boolean loaded = false;
        for (PropertySourceLoader propertySourceLoader : propertySourceLoaders) {
            if (loadDefaultProperties(defaultPropertiesResource, resource, propertySourceLoader, configurableEnvironment)) {
                loaded = true;
                break;
            }
        }
        return loaded;
    }

    private boolean loadDefaultProperties(String defaultPropertiesResource, Resource resource,
                                          PropertySourceLoader propertySourceLoader,
                                          ConfigurableEnvironment configurableEnvironment) {
        boolean loaded = false;
        try {
            URL url = resource.getURL();
            String resourceLocation = url.getPath();
            String fileExtension = getExtension(resourceLocation);
            String[] fileExtensions = propertySourceLoader.getFileExtensions();
            if (matches(fileExtension, fileExtensions)) {

                List<PropertySource<?>> propertySources = propertySourceLoader.load(resourceLocation, resource);
                logger.debug("'defaultProperties' resource [location: {}] loads into {} PropertySource", resourceLocation, propertySources.size());
                for (PropertySource propertySource : propertySources) {
//                    if (propertySource instanceof EnumerablePropertySource) {
//                        // merge((EnumerablePropertySource) propertySource, defaultProperties);
//                        loaded = true;
//                    }
                    configurableEnvironment.getPropertySources().addLast(propertySource);

                }
                loaded = true;
            }
        } catch (IOException e) {
            logger.error("'defaultProperties' resource [{}] failed to load", defaultPropertiesResource, e);
        }
        return loaded;
    }

    private String getExtension(String resourceLocation) {
        int index = resourceLocation.lastIndexOf(".");
        if (index == -1) {
            return null;
        }
        String extension = resourceLocation.substring(index + 1);
        return extension;
    }

    private boolean matches(String fileExtension, String[] fileExtensions) {
        return ObjectUtils.containsElement(fileExtensions, fileExtension);
    }
}