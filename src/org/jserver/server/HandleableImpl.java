package org.jserver.server;

import org.jserver.http.Request;
import org.jserver.util.ConfReader;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by yuantao on 2014/6/18.
 */
public class HandleableImpl implements Handleable {
    @Override
    public void handle(SocketChannel channel) throws IOException {
        String reuqestHead = read(channel);
        Request request = new Request(reuqestHead);
        String resoure = request.getRequestUrl();
        try {
            write(channel, new File(ConfReader.get("path")+resoure));
        }catch (FileNotFoundException e){
            write(channel, new File(ConfReader.get("path")+"/"+ConfReader.get("page.404")));
        }
        channel.close();
    }

    private void write(SocketChannel channel, File file) throws IOException {
        FileChannel fileChannel = new FileInputStream(file).getChannel();
        write(channel, fileChannel);
    }

    private String read(SocketChannel channel) throws IOException {
        ByteBuffer buff = ByteBuffer.allocate(1024);
        StringBuffer str = new StringBuffer("");
        // 开始读取数据
        while (channel.read(buff) > 0) {
            buff.flip();
            str.append(new String(buff.array()));
            buff.clear();
        }
        return str.toString();
    }

    private void write(SocketChannel socketChannel, FileChannel fileChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (fileChannel.read(buffer) > 0) {
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
    }

    private void write(SocketChannel channel, String out) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(out.getBytes());
        channel.write(byteBuffer);
    }

}
