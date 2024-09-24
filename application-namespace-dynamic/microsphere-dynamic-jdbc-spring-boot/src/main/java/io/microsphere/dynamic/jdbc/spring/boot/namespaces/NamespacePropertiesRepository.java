package io.microsphere.dynamic.jdbc.spring.boot.namespaces;

import io.microsphere.dynamic.jdbc.spring.boot.namespaces.property.NamespaceProperties;

 class NamespacePropertiesRepository {
    private NamespaceProperties namespaceProperties;
    private NamespacePropertiesRepository(){};
    public final static NamespacePropertiesRepository INSTANCE = new NamespacePropertiesRepository();

    public void  setNamespaceProperties(NamespaceProperties namespaceProperties){
        this.namespaceProperties = namespaceProperties;
    }

    public NamespaceProperties  getNamespaceProperties(){
       return this.namespaceProperties;
    }
}
