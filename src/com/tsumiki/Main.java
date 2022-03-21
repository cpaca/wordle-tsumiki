package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args){
        TimeQualifier();
    }

    private static final int iters = 10000;
    private static void TimeQualifier(){
        long time = 0;
        WordleGuesser wordle = new WordleGuesser(GetWords());
        char[][] words = GetWords();
        Random r = new Random(System.nanoTime());
        for(int i = 0; i < iters; i++){
            int wordIndex = r.nextInt(words.length);
            char[] word = words[wordIndex];
            time -= System.nanoTime();
            wordle.QualifyGuess(word);
            time += System.nanoTime();
        }

        System.out.printf("Maximum value LONG: %,d\n", Long.MAX_VALUE);
        System.out.printf("Current time:       %,d\n", System.nanoTime());
        System.out.printf("Total time taken:   %,d\n", time);
        time /= iters;
        System.out.printf("Average time taken: %,d\n", time);
    }

    private static void TestProtoQualifier(){

        // Code from when I first made the qualifier
        // Modify it a tiny bit if you want to test the proto-qualifier
        WordleGuesser wordle = new WordleGuesser(GetWords());
        Consumer<String> qualifier = (String s) ->{
            System.out.println("Quality of " + s + ": " + wordle.QualifyGuess(s.toCharArray()));
        };

        qualifier.accept("adieu");
        qualifier.accept("among");
        qualifier.accept("track");
        qualifier.accept("audio");
        qualifier.accept("night");
        qualifier.accept("peace");

        System.out.println();
        System.out.println("Testing the saute results.");
        // Quick test case
        wordle.ApplyGuess("adieu".toCharArray(), new byte[]{1,0,0,1,1});
        qualifier.accept("snort");
        wordle.ApplyGuess("snort".toCharArray(), new byte[]{2,0,0,0,1});
        qualifier.accept("among");
        qualifier.accept("saute");
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
