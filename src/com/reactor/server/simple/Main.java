package com.reactor.server.simple;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Thread(new Reactor(2333)).start();
    }
}
