package org.jserver.http;

/**
 * Created by yuantao on 2014/6/22.
 */
public class Request {
    private String requestHead;
    private String requestUrl;
    public Request(String requestHead){
        this.requestHead = requestHead;
        parseHead(requestHead);
    }

    private void parseHead(String requestHead){
        this.requestUrl = requestHead.toString().split("\n", -1)[0].split(" ")[1];
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

}
