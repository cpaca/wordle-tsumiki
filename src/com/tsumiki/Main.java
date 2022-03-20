package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args){
        TestApplyGuess();
        TestProtoGuess();
    }

    private static final int iters = 1000;
    private static void TestApplyGuess(){
        long time = 0;
        for(int i = 0; i < iters; i++){
            WordleGuesser wordle = new WordleGuesser(GetWords());
            time -= System.nanoTime();
            wordle.ApplyGuess("adieu".toCharArray(), new byte[]{1,0,0,1,1});
            time += System.nanoTime();
        }
        time /= iters;
        System.out.println("Method ApplyGuess");
        System.out.printf("Average time: %,d\n", time);
    }

    private static void TestProtoGuess(){
        long time = 0;
        for(int i = 0; i < iters; i++){
            WordleGuesser wordle = new WordleGuesser(GetWords());
            time -= System.nanoTime();
            wordle.ProtoApplyGuess("adieu".toCharArray(), new byte[]{1,0,0,1,1});
            time += System.nanoTime();
        }
        time /= iters;
        System.out.println("Method ProtoApplyGuess");
        System.out.printf("Average time: %,d\n", time);
    }

    private static char[][] GetWords(){
        ArrayList<String> wordList = new ArrayList<>();
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader("words.txt"));
            String line = reader.readLine();
            while(line != null){
                wordList.add(line);

                line = reader.readLine();
            }
        } catch (IOException ioe){ // also file not found exception apparantly
            ioe.printStackTrace();
        } finally {
            try{
                if(reader != null){
                    reader.close();
                }
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        }

        char[][] words = new char[wordList.size()][5];
        AtomicInteger i = new AtomicInteger();

        wordList.forEach((String s) ->{
            char[] word = s.toCharArray();
            words[i.get()] = word;

            i.getAndIncrement();
        });
        return words;
    }
}
