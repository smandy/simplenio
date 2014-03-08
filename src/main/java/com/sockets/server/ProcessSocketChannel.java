package com.sockets.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Date;

public class ProcessSocketChannel implements Runnable {


    public static void main(String[] args) throws IOException {
        int port = 20000;
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().setReuseAddress(true);
        server.socket().bind(new InetSocketAddress(port));
        SocketChannel x = server.accept();
        new ProcessSocketChannel(x).run();
    }

 private SocketChannel socketChannel;
 private int BUFFER_SIZE = 1024;

 public ProcessSocketChannel(SocketChannel socketChannel) {
  this.socketChannel = socketChannel;
  Thread thread = new Thread(this);
  thread.start();
 }

 public void run() {
     System.out.println("Connection received from "
    + socketChannel.socket().getInetAddress().getHostAddress());
     readMessage();
     sendMessage("This is the server!!");
 }

 void sendMessage(String msg) {
  String fullmessage = new Date().toString() + " > " + msg;
  ByteBuffer buf = ByteBuffer.allocate(fullmessage.getBytes().length);
  buf.clear();
  buf.put(fullmessage.getBytes());
  buf.flip();
  while (buf.hasRemaining()) {
   try {
    socketChannel.write(buf);
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
 }

 void readMessage() {
  ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
  Charset charset = Charset.forName("us-ascii");
  CharsetDecoder decoder = charset.newDecoder();
  CharBuffer charBuffer;
  try {
   int bytes = socketChannel.read(byteBuffer);
   byteBuffer.flip();
   charBuffer = decoder.decode(byteBuffer);
   String result = charBuffer.toString();
   System.out.println(result);
  } catch (IOException e) {
   e.printStackTrace();
  } finally {
   byteBuffer = null;
   charset = null;
   decoder = null;
   charBuffer = null;
  }
 }
}
