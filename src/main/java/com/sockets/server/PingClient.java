package com.sockets.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class PingClient {

    static final int REPORT_EVERY = 10;

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress("skye", 30000);
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
            if (idx % REPORT_EVERY == 0) {
                long last = System.nanoTime();
                long dur = last - time;
                System.out.println( String.format("idx=%s cmp=%s ops/sec=%s us per op = %s", idx, cmp, 2 * REPORT_EVERY * 1e9 / dur, dur / (2 * 1e3 * REPORT_EVERY )));
                time = last;
            }
            idx += 1;
        }
    }
}
