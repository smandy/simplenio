package com.sockets.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Util {
    static void writeAll(SocketChannel sc, ByteBuffer bb) throws IOException {
        while (bb.hasRemaining() ) {
            int x = sc.write(bb);
        }
    }

    static void readAll(SocketChannel sc, ByteBuffer bb) throws IOException {
        while(bb.remaining()>0) {
            if ( sc.read(bb) <0) break;
        }
    }
}
