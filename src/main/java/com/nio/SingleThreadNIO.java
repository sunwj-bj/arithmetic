package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: sunwj
 * Date: 2020/9/16 23:46
 * Content:  本示例为 selector 单线程版本,一个线程既监听连接也监听数据
 */
public class SingleThreadNIO {

    //step 01  初始化全局变量  selector  server  port
    private ServerSocketChannel server=null;
    private Selector selector =null;
    private int port =8092;

    //step 02  initServer  实例化 server  selector  并将server注册到select的op_accept上面
    private void initServer() throws IOException {
        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        server.configureBlocking(false);

        selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);//服务端的accept注册到selector
    }

    //step 03  start
    // while (true)  {  while(select.select(0）>0) {  //当有02步骤的服务端注册到select  ,
    // 取出  所有的 selectionKeys
    private void start() throws IOException {
        initServer();
        System.out.println("端口号为："+ port +" ，服务正在启动中... ");
        while (true){
            //如果selector中有值，说明 server 端有 accept事件到达 select
            while (selector.select(0)>0){
                Set<SelectionKey> keySets = selector.selectedKeys();
                Iterator<SelectionKey> iter = keySets.iterator();

                while(iter.hasNext()){
                    SelectionKey key = iter.next();
                    iter.remove();
                    if(key.isAcceptable()){  //如果select 是服务端触发的accept
                        acceptHandler(key);
                    }else if(key.isReadable()){ //如果 select 是客户端触发的 readable
                        readHandler(key);
                    }
                }
            }
        }
    }


    //04 acceptable
    // 通过   SelectionKey 获取 serverSocketChannel
    // 配置此serverSocketChannel的客户端socketChannel为非阻塞状态
    // 将socketChannel注册为 select 的 readable 并附赠 byteBuffer
    private void acceptHandler(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
        SocketChannel clientChannel = ssc.accept();
        clientChannel.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(20000);//此处在堆分配内存，不是栈
        clientChannel.register(selector,SelectionKey.OP_READ,byteBuffer);
        System.out.println("----------------------------------");
        System.out.println("新客户端"+clientChannel.getRemoteAddress());
        System.out.println("----------------------------------");
    }


    //05 处理客户端readable事件
    private void readHandler(SelectionKey key)  {
        SocketChannel client = (SocketChannel)key.channel();
        ByteBuffer byteBuffer = (ByteBuffer)key.attachment();
        byteBuffer.clear();
        int count=0; //socketChannel中获取到的个数

        try {
            while (true) {
                count = client.read(byteBuffer);
                if (count > 0) {  // 如果能够读到 客户端 的值  ，将此值 会写给客户端
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        client.write(byteBuffer);
                    }
                    byteBuffer.clear();      //打扫卫生
                } else if (count == 0) {         //如果读不到值 ，停止当前循环
                    break;
                } else {                       //如果是-1 也就是客户端不连接了 , 关闭当前客户端
                    System.out.println(" 客户端断开连接 "+ client.getRemoteAddress());
                    client.close();
                    break;
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        SingleThreadNIO singleThreadNIO = new SingleThreadNIO();
        singleThreadNIO.start();
    }
}
