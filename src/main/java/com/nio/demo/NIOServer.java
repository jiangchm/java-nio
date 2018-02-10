package com.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
	
	private Selector selector ;
	
	//初始化服务端
	public void initServer(int port) throws IOException{
		ServerSocketChannel ssc = ServerSocketChannel.open() ;
		ssc.configureBlocking(false);
		ssc.socket().bind(new InetSocketAddress(9999));
		selector = Selector.open() ;
		ssc.register(selector, SelectionKey.OP_ACCEPT) ;
		System.out.println("服务端启动成功");
	}
	
	//轮询服务端的监听selector的注册事件
	public void listen() throws IOException{
		System.out.println("服务端开始监听.....");
		while(true){
			int n = selector.select() ;
			if(n == 0){
				continue ;
			}
			Iterator<SelectionKey> it = selector.selectedKeys().iterator() ;
			while(it.hasNext()){
				SelectionKey key = it.next() ;
				System.out.println(key);
				it.remove();
				//客户端发来数据，服务端读取
				//客户端连接服务端事件
				if(key.isAcceptable()){
					ServerSocketChannel server = (ServerSocketChannel) key.channel() ;
					SocketChannel channel = server.accept() ;
					channel.configureBlocking(false) ;
					////在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限
					channel.register(selector, SelectionKey.OP_READ) ;
				}
				if(key.isReadable()){
					read(key) ;
				}
			}
		}
	}
	
	public void read(SelectionKey key) throws IOException{
		SocketChannel channel = (SocketChannel) key.channel() ;
		ByteBuffer buffer = ByteBuffer.allocate(10) ;
		channel.read(buffer) ;
		System.out.println("服务端收到的数据:"+new String(buffer.array())) ;
		
	}
	
	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer() ;
		server.initServer(9999);
		server.listen();
	}

}
