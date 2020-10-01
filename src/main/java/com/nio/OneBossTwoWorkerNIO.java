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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: sunwj
 * Date: 2020/9/18 7:14
 * Content:
 *  多线程 版本的selector
 */
public class OneBossTwoWorkerNIO {

    private ServerSocketChannel server =null;
    private Selector selectorBoss =null;
    private Selector selectorWork1 =null;
    private Selector selectorWork2 =null;
    private int port=8094;

    private void initServer(){
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            selectorBoss = Selector.open();
            selectorWork1 =Selector.open();
            selectorWork2 =Selector.open();

            server.register(selectorBoss,SelectionKey.OP_ACCEPT);
            System.out.println("---initServer()---- mainThread initServer is starting ... ");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //实例化 boss  worker1  worker2 线程 并启动 运行
    public static void main(String[] args) {
        OneBossTwoWorkerNIO mainThread = new OneBossTwoWorkerNIO();
        mainThread.initServer();
        new Nio4SelectorThread(mainThread.selectorBoss,2).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Nio4SelectorThread(mainThread.selectorWork1).start();
        new Nio4SelectorThread(mainThread.selectorWork2).start();
        System.out.println("---main()----服务器启动了 " + "当前线程是 "+ Thread.currentThread().getName());

    }

}




class Nio4SelectorThread extends Thread {
    // 是否是boss线程
    private  boolean isBoss =false;
    private Selector selector =null;
    private static int workerCnt=0;
    private static AtomicInteger indexClientConn = new AtomicInteger();
    private static BlockingQueue<SocketChannel>[] queueWorker;
    private static AtomicInteger indexWorkerConn =new AtomicInteger();
    private int workerIndex;
    //boss的构造函数
    public Nio4SelectorThread(Selector tmpSelector,int count){
        this.isBoss = true;
        this.selector =tmpSelector;
        this.workerCnt= count;
        queueWorker = new LinkedBlockingQueue[this.workerCnt];
        for(int i=0;i<count;i++){
            queueWorker[i] =new LinkedBlockingQueue<>();
        }
        System.out.println("当前线程是 "+ Thread.currentThread().getName() + " 初始化 bossSelect 线程 ");
    }
    //worker 构造函数
    public Nio4SelectorThread(Selector tmpSelector){
        this.selector=tmpSelector;
        workerIndex = indexWorkerConn.getAndIncrement() % workerCnt;
        System.out.println("当前线程是 "+ Thread.currentThread().getName()+ " 初始化 workerSelect 线程 workerIndex is "+ workerIndex);
    }

    @Override
    public void run(){
        try {
            while(true){
                while(selector.select(6000)>0){
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> iter = keySet.iterator();
                    if(iter.hasNext()){
                        SelectionKey key = iter.next();
                        iter.remove();
                        if(key.isAcceptable()){
                            acceptHandler(key);
                        }else if (key.isReadable()){
                            readHandler(key);
                        }else{
                        }
                    }
                }
                System.out.println("当前线程是 "+ Thread.currentThread().getName());
                try {
                    if(!isBoss && !queueWorker[workerIndex].isEmpty() ){
                        SocketChannel client = queueWorker[workerIndex].take();
                        ByteBuffer buffer = ByteBuffer.allocate(40000);
                        client.register(this.selector,SelectionKey.OP_READ,buffer);
                        System.out.println("-------------------------------------------");
                        System.out.println("将客户端读事件注册到指定selector上面 ；此客户端地址是" + client.getRemoteAddress());
                        System.out.println("-------------------------------------------");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptHandler(SelectionKey key){
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);
            System.out.println(" 当有客户端连接到服务端（selector）");
            int num = indexClientConn.getAndIncrement() % workerCnt;
            queueWorker[num].add(client);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void readHandler(SelectionKey key){
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer byteBuffer =(ByteBuffer) key.attachment();
        byteBuffer.clear();
        try {
            while (true){
                int read = client.read(byteBuffer);
                if(read>0){
                    byteBuffer.flip();
                    client.write(byteBuffer);
                    byteBuffer.clear();
                    break;
                }else if(read==0){  //没有数据 退出循环
                    break;
                }else{     //客户端断开连接  -1
                    System.out.println("客户端断开连接");
                    client.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
