package com.zch.rxjava.getstarted.unconditional_network_request_polling;

/**
 * Created by zch on 2018/5/3.
 */
public class TranslationBean {

    public int status;
    public Content content;

    public static class Content {

        public String from;
        public String to;
        public String vendor;
        public String out;
        public int errNo;
    }
}