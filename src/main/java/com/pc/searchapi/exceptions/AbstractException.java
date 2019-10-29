package com.pc.searchapi.exceptions;

import com.fasterxml.jackson.annotation.JsonView;
import com.pc.searchapi.model.Views;
import org.apache.http.HttpException;

    public abstract class AbstractException extends HttpException {

        @JsonView(Views.Public.class)
        private int code;

        public AbstractException(int code,String msg) {
            super(msg);
            this.code = code;

        }

        public AbstractException(int code,String msg, Throwable e) {
            super(msg, e);
            this.code = code;
        }


        public int getCode() {
            return code;
        }

        @Override
        @JsonView(Views.Public.class)
        public String getMessage() {
            return super.getMessage();
        }
}
