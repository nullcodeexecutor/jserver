package org.jserver.server;

import sun.awt.windows.ThemeReader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by yuantao on 2014/6/18.
 */
public class Server {
    private static Server server = null;

    private int port = 0;
    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;

    private Server(){
    }

    public static Server newServer(){
        if(server == null){
            server = new Server();
        }
        return server;
    }

    public Server bind(int port){
        this.port = port;
        return this;
    }

    public void start(){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(this.port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(true){
                int n = selector.select();
                if(n<=0){
                    continue;
                }
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey selectionKey = it.next();
                    it.remove();
                    try {
                        if (selectionKey.isValid() && selectionKey.isAcceptable()) {
                            // 调用accept方法接收连接，产生服务器段的SocketChennal
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            // 设置采用非阻塞模式
                            socketChannel.configureBlocking(false);
                            // 将该SocketChannel注册到selector
                            SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            key.selector().wakeup();
                        } else if (selectionKey.isValid() && selectionKey.isReadable() && selectionKey.isWritable()) {
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            new HandleableImpl().handle(channel);
//                                new ProcessThread(channel, new HandleableImpl()).start();
                        } else if (selectionKey.isConnectable()) {
                            System.out.println("isConnectable");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ProcessThread extends Thread{
        SocketChannel socketChannel = null;
        Handleable handleable = null;

        ProcessThread(SocketChannel socketChannel, Handleable handleable){
            this.socketChannel = socketChannel;
            this.handleable = handleable;
        }
        @Override
        public void run() {
            try {
                this.handleable.handle(socketChannel);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
