package com.crazymaker.gateway.core.api.error;

import com.crazymaker.gateway.core.api.constant.ResponseCode;

public class BizException extends RuntimeException {

    private static final long serialVersionUID = -5658789202563433456L;

    public BizException() {
    }

    protected ResponseCode code;

    public BizException(String message, ResponseCode code) {
        super(message);
        this.code = code;
    }

    public BizException(String message, Throwable cause, ResponseCode code) {
        super(message, cause);
        this.code = code;
    }

    public BizException(ResponseCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BizException(String message, Throwable cause,
                        boolean enableSuppression, boolean writableStackTrace, ResponseCode code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public ResponseCode getCode() {
        return code;
    }

}
