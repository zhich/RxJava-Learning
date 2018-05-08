package com.zch.rxjava.getstarted.nesting_network_request;

/**
 * Created by zch on 2018/5/5.
 */
public class LoginBean {

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