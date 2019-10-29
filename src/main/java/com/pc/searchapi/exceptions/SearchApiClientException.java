package com.pc.searchapi.exceptions;

/**
 * Created by chitra_chitralekha on 10/29/19.
 */
public class SearchApiClientException extends AbstractException {


    public SearchApiClientException(int code, String msg) {
        super(code, msg);
    }

    public SearchApiClientException(int code, String msg,Throwable e) {
        super(code, msg);
    }
}
