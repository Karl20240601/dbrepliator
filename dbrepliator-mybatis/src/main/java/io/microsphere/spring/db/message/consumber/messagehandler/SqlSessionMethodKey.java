package io.microsphere.spring.db.message.consumber.messagehandler;

import java.util.Arrays;

public class SqlSessionMethodKey {
    private final Class<?>[] classes;

    public SqlSessionMethodKey(Class<?>[] classes) {
        this.classes = classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlSessionMethodKey)) return false;
        SqlSessionMethodKey that = (SqlSessionMethodKey) o;
        return classArrayEquals(classes, that.classes);
    }

    private boolean classArrayEquals(Class<?>[] classes1, Class<?>[] classes2) {
        if (classes1 == null && classes2 == null) {
            return true;
        } else if (classes1 == null || classes2 == null) {
            return false;
        }

        if (classes1.length != classes2.length) {
            return false;
        }

        for (int i = 0; i < classes1.length; i++) {
            if (classes1[i] != classes2[i] && !classes2[i].isAssignableFrom(classes1[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(classes);
    }

    @Override
    public String toString() {
        return "SqlSessionMethodKey{" +
                "classes=" + Arrays.toString(classes) +
                '}';
    }
}
