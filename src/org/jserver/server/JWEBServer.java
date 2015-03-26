package org.jserver.server;

/**
 * Created by yuantao on 2014/6/17.
 */
public class JWEBServer {

    public static void main(String[] args) {
        Server.newServer().bind(80).start();
    }

}
