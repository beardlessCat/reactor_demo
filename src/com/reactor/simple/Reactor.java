package com.reactor.simple;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class Reactor {
    Selector selector ;
    ServerSocketChannel serverSocketChannel;
    public Reactor(int port) throws IOException {
        selector = Selector.open();
    }

    void dispatch (){

    }

    void select(){

    }
}
