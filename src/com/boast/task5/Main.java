/*
 * Main.java 14/08/2017
 *
 * Created by Bondarenko Oleh
 */


package com.boast.task5;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int threadsCount = 100;
        int mapSize = 100_000;
        HashMap<String, String> hashMap = new HashMap<>(0, 0.75f);
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>(0, 0.75f);
        Thread[] threads = new Thread[threadsCount];

        System.out.println(threadsCount + " threads, map size: " + mapSize);

        write(threads, hashMap, mapSize);
        write(threads, concurrentHashMap, mapSize);

        read(threads, hashMap, mapSize);
        read(threads, concurrentHashMap, mapSize);
    }

    static void write(Thread[] threads, Map map, int mapSize) throws InterruptedException {
        Long timer = System.currentTimeMillis();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Write(map, mapSize));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Write: " + map.getClass().getName() + ": " + (System.currentTimeMillis() - timer) + " ms");
    }
    static void read(Thread[] threads, Map map, int mapSize) throws InterruptedException {
        Long timer = System.currentTimeMillis();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Read(map, mapSize));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Read: " + map.getClass().getName() + ": " + (System.currentTimeMillis() - timer) + " ms");
    }
}

class Write implements Runnable{
    private Map map;
    private int mapSize;

    Write(Map map, int mapSize){
        this.map = map;
        this.mapSize = mapSize;
    }

    @Override
    public void run(){
        for (Integer i = 1; i <= mapSize; i++){
            map.put(i.toString(), i.toString());
            //System.out.println(Thread.currentThread() + " " + i);
        }
    }
}

class Read implements Runnable{
    private Map map;
    private int mapSize;

    Read(Map map, int mapSize){
        this.map = map;
        this.mapSize = mapSize;
    }

    @Override
    public void run(){
        for (Integer i = 1; i <= mapSize; i++){
            map.get(i.toString());
            //System.out.println(Thread.currentThread() + " " + i);
        }
    }
}
