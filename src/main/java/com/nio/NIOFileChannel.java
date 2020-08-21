package com.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO中FileChannel的使用
 * 字符串->Buffer->Channel->文件
 * 文件->Channel->Buffer->字符串
 */
public class NIOFileChannel {
    public static void main(String[] args) throws IOException {
        str2File();
        file2Str();
        //oneFile2OtherFile();
        oneFile2OtherFileWithoutBuffer();
    }

    private static void file2Str() throws IOException {
        //创建输入流
        FileInputStream fileInputStream = new FileInputStream("D:\\file01.txt");
        //获取channel
        FileChannel channel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //把数据从Channel读到缓冲区
        channel.read(byteBuffer);
        //读取数据
        System.out.println(new String(byteBuffer.array()));
        //关闭流
        fileInputStream.close();
    }

    private static void str2File() throws IOException {
        String str = "hello，孙文杰！";
        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\file01.txt");
        //通过fileOutputStream获取对应的FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();
        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入缓冲区
        byteBuffer.put(str.getBytes());
        //buffer反转
        byteBuffer.flip();
        //channel读取缓冲区数据
        fileChannel.write(byteBuffer);
        //关闭流
        fileOutputStream.close();
    }

    /**
     * 使用了缓冲区的文件拷贝
     * @throws IOException
     */
    private static void oneFile2OtherFile() throws IOException{
        FileInputStream fileInputStream = new FileInputStream("D:\\file01.txt");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\file02.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true){
            byteBuffer.clear();
            int read = fileInputStreamChannel.read(byteBuffer);
            System.out.println("read="+read);
            //返回-1表示读完了
            if (read==-1){
                break;
            }
            byteBuffer.flip();
            fileOutputStreamChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

    /**
     *
     * @throws IOException
     */
    private static void oneFile2OtherFileWithoutBuffer() throws IOException{
        FileInputStream fileInputStream = new FileInputStream("D:\\file01.txt");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\file02.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
        fileInputStreamChannel.transferTo(0,fileInputStreamChannel.size(),fileOutputStreamChannel);
        fileOutputStream.close();
        fileInputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
