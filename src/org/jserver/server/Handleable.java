package org.jserver.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Created by yuantao on 2014/6/18.
 */
public interface Handleable {

    void handle(SocketChannel channel) throws IOException;

}
