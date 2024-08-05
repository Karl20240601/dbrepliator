package io.microsphere.spring.db.support.enums;

import java.io.Serializable;

public enum StatementEnum implements Serializable {
    STATEMENT,
    PREPAREEDSTATEMENT,
    BATCH_STATEMENT,
    BATCH_PREPAREEDSTATEMENT;
}
