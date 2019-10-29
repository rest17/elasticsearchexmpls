package com.pc.searchapi.exceptions;

/**
 * Created by chitra_chitralekha on 10/29/19.
 */
public class SearchApiInternalExcpetion extends AbstractException{

    public SearchApiInternalExcpetion(int code, String msg) {
        super(code, msg);
    }

    public SearchApiInternalExcpetion(int code, String msg,Throwable e) {
        super(code, msg);
    }
}
