package com.hcj.maker.meta;

/**
 * @Author:HCJ
 * @DateTime:2024/3/11
 * @Description:
 **/
public class MetaException extends RuntimeException {
    public MetaException(String message){
        super(message);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }
}
