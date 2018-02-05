package com.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {
	
	private Selector selector ;
	
	public void initClient(String ip,int port) throws IOException{
		SocketChannel channel = SocketChannel.open() ;
		selector = Selector.open();
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress("localhost", 9999)) ;
		channel.register(selector, SelectionKey.OP_CONNECT) ;
	}
	
	public void listen() throws IOException{
		System.out.println("客户端监听");
		while(true){
			selector.select() ;
			Iterator<SelectionKey> keys = selector.keys().iterator() ;
			while(keys.hasNext()){
				SelectionKey key = keys.next() ;
				if(key.isConnectable()){
					SocketChannel channel = (SocketChannel) key.channel() ;
					if(channel.isConnectionPending()){
						channel.finishConnect() ;
					}
					channel.configureBlocking(false);
					System.out.println("客户端已建立和服务端的连接");
					channel.write(ByteBuffer.wrap("客户端你好".getBytes()));
					channel.register(selector, SelectionKey.OP_READ);
				}
				if(key.isReadable()){
					SocketChannel channel = (SocketChannel) key.channel() ;
					if(channel.isConnectionPending()){
						channel.finishConnect() ;
					}
					ByteBuffer bb = ByteBuffer.allocate(1) ;
					channel.read(bb) ;
					System.out.println("客户端收到:"+new String(bb.array()));
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		NIOClient client = new NIOClient() ;
		client.initClient("localhost", 9999);
		client.listen(); 
	}

}
