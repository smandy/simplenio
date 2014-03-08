package com.sockets.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by andy on 0w 7/03/14.
 */
public class PongServer {
    public static void main(String[] args ) throws IOException {
        new PongServer().run();
    }

    private void run() throws IOException {
        ServerSocket ss = new ServerSocket();
        InetSocketAddress x = new InetSocketAddress("localhost", 30000);

        while(true) {
            ServerSocketChannel s = ServerSocketChannel.open();

            s.bind(x);
            SocketChannel sc = s.accept();
            serve(sc);
        }
    }

    private void serve(SocketChannel sc) throws IOException {
        System.out.println(String.format("Serving %s", sc));
        ByteBuffer bb = ByteBuffer.allocateDirect(8).order(ByteOrder.LITTLE_ENDIAN);
        sc.socket().setReceiveBufferSize(8);
        while(true) {
            bb.clear();
            Util.readAll(sc, bb);
            bb.flip();
            long toPing = bb.getLong();
            bb.clear();
            bb.putLong(toPing);
            bb.flip();
            Util.writeAll(sc, bb);
        }
    }
}
