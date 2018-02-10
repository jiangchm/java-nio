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
	
	//��ʼ�������
	public void initServer(int port) throws IOException{
		ServerSocketChannel ssc = ServerSocketChannel.open() ;
		ssc.configureBlocking(false);
		ssc.socket().bind(new InetSocketAddress(9999));
		selector = Selector.open() ;
		ssc.register(selector, SelectionKey.OP_ACCEPT) ;
		System.out.println("����������ɹ�");
	}
	
	//��ѯ����˵ļ���selector��ע���¼�
	public void listen() throws IOException{
		System.out.println("����˿�ʼ����.....");
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
				//�ͻ��˷������ݣ�����˶�ȡ
				//�ͻ������ӷ�����¼�
				if(key.isAcceptable()){
					ServerSocketChannel server = (ServerSocketChannel) key.channel() ;
					SocketChannel channel = server.accept() ;
					channel.configureBlocking(false) ;
					////�ںͿͻ������ӳɹ�֮��Ϊ�˿��Խ��յ��ͻ��˵���Ϣ����Ҫ��ͨ�����ö���Ȩ��
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
		System.out.println("������յ�������:"+new String(buffer.array())) ;
		
	}
	
	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer() ;
		server.initServer(9999);
		server.listen();
	}

}
