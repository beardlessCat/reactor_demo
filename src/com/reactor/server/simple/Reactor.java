package com.reactor.server.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable{
    Selector selector ;
    ServerSocketChannel serverSocketChannel;

    /**
     * Reactor初始化
     * @param port
     * @throws IOException
     */
    public Reactor(int port) throws IOException {
        ///打开一个Selector
        selector = Selector.open();
        ////建立一个Server端通道
        serverSocketChannel = ServerSocketChannel.open(); //打开通
        serverSocketChannel.configureBlocking(false); //设置通道为非阻塞模式
        serverSocketChannel.bind(new InetSocketAddress(port)); //绑定端口
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        selectionKey.attach(new Acceptor(serverSocketChannel, selector));
    }

    void dispatch(SelectionKey selectionKey){
        Runnable r = (Runnable) (selectionKey.attachment()); //这里很关键，拿到每次selectKey里面附带的处理对象，然后调用其run，这个对象在具体的Handler里会进行创建，初始化的附带对象为Acceptor（看上面构造器）
        //调用之前注册的callback对象
        if (r != null) {
            r.run();
        }
    }


    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select(); //就绪事件到达之前，阻塞
                Set selected = selector.selectedKeys(); //拿到本次select获取的就绪事件
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    //这里进行任务分发
                    dispatch((SelectionKey) (it.next()));
                }
                selected.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
