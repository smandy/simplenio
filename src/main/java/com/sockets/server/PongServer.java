package com.sockets.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by andy on 0w 7/03/14.
 */
public class PongServer {
    public static void main(String[] args) throws IOException {
        new PongServer().run();
    }

    private void run() throws IOException {
        System.out.println("Woohoo");
        InetSocketAddress x = new InetSocketAddress("192.168.1.83", 30000);
        while (true) {
            ServerSocketChannel s = ServerSocketChannel.open();
            s.socket().setReceiveBufferSize(8);
            s.socket().bind(x);
            SocketChannel sc = s.accept();
            serve(sc);
        }
    }

    private void serve(SocketChannel sc) throws IOException {
        System.out.println(String.format("Serving %s", sc));
        ByteBuffer bb = ByteBuffer.allocateDirect(8);
        sc.socket().setReceiveBufferSize(8);
        while (true) {
            bb.clear();
            Util.readAll(sc, bb);
            bb.flip();
            long toPing = bb.getLong();
            if (toPing % 1000 == 0) {
                System.out.println("Got " + toPing);
            }
            ;
            bb.clear();
            bb.putLong(toPing);
            bb.flip();
            Util.writeAll(sc, bb);
        }
    }
}
