package io.microsphere.dynamic.jdbc.spring.boot.namespaces.property;


public class NamespaceProperty implements Cloneable {
    private String NameSpaceName;
    private String springProfieActive;

    public String getNameSpaceName() {
        return NameSpaceName;
    }

    public void setNameSpaceName(String nameSpaceName) {
        NameSpaceName = nameSpaceName;
    }

    public String getSpringProfieActive() {
        return springProfieActive;
    }

    public void setSpringProfieActive(String springProfieActive) {
        this.springProfieActive = springProfieActive;
    }
}
