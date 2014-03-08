package com.sockets.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by andy on 08/03/14.
 */
public class PingClient {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket();
        InetSocketAddress address = new InetSocketAddress("localhost", 30000);
        SocketChannel socketChannel = SocketChannel.open(address);
        System.out.println("WOohoo connected to " + socketChannel);
        ByteBuffer bb = ByteBuffer.allocateDirect(8);
        long time = System.nanoTime();
        long idx = 0;
        while (true) {
            bb.clear();
            bb.putLong(idx);
            bb.flip();
            Util.writeAll(socketChannel, bb);
            bb.clear();
            Util.readAll( socketChannel, bb);
            bb.flip();
            long cmp = bb.getLong();
            if (idx % 50000 == 0) {
                long last = System.nanoTime();
                long dur = last - time;
                System.out.println( String.format("idx=%s cmp=%s ops/sec=%s us per op = %s", idx, cmp, 2 * 50000.0 * 1e9 / dur, dur / (2 * 1e3 * 50000.0 )));
                time = last;
            }
            idx += 1;
        }
    }
}
