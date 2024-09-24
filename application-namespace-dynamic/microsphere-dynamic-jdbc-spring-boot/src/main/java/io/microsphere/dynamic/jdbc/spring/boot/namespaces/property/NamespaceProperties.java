package io.microsphere.dynamic.jdbc.spring.boot.namespaces.property;


import java.util.List;

public class NamespaceProperties implements Cloneable {
    private boolean enable;
    private List<NamespaceProperty> namespaces;
}
