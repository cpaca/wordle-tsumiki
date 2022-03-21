package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args){
        TestProtoQualifier();
        System.out.printf("Maximum value LONG:  %,d\n", Long.MAX_VALUE);
        System.out.printf("Current time:        %,d\n", System.nanoTime());
        TimeQualifier();
        TimeProtoQualifier();

    }

    private static final int iters = 10000;
    private static void TimeQualifier(){
        System.gc();
        System.runFinalization(); // for some reason without these the proto time was MUCH longer

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

        System.out.printf("Qual Total time taken:    %,d\n", time);
        System.out.printf("Qual Average time taken:  %,d\n", time/iters);
    }

    private static void TimeProtoQualifier(){
        System.gc();
        System.runFinalization(); // for some reason without these the proto time was MUCH longer

        long time = 0;
        WordleGuesser wordle = new WordleGuesser(GetWords());
        char[][] words = GetWords();
        Random r = new Random(System.nanoTime());
        for(int i = 0; i < iters; i++){
            int wordIndex = r.nextInt(words.length);
            char[] word = words[wordIndex];
            time -= System.nanoTime();
            wordle.ProtoQualifyGuess(word);
            time += System.nanoTime();
        }

        System.out.printf("Proto Total time taken:   %,d\n", time);
        System.out.printf("Proto Average time taken: %,d\n", time/iters);
    }

    private static void TestProtoQualifier(){

        // Code from when I first made the qualifier
        // Modify it a tiny bit if you want to test the proto-qualifier
        WordleGuesser wordle = new WordleGuesser(GetWords());
        Predicate<String> qualifier = (String s) ->{
            long qual1 = wordle.QualifyGuess(s.toCharArray());
            long protoqual = wordle.ProtoQualifyGuess(s.toCharArray());
            if(qual1 != protoqual){
                System.out.println("Failed qualification on " + s);
                return false;
            }
            return true;
        };

        boolean out = qualifier.test("adieu");
        out = out && qualifier.test("among");
        out = out && qualifier.test("track");
        out = out && qualifier.test("audio");
        out = out && qualifier.test("night");
        out = out && qualifier.test("peace");

        System.out.println();
        // Quick test case
        wordle.ApplyGuess("adieu".toCharArray(), new byte[]{1,0,0,1,1});
        out = out && qualifier.test("snort");
        wordle.ApplyGuess("snort".toCharArray(), new byte[]{2,0,0,0,1});
        out = out && qualifier.test("among");
        out = out && qualifier.test("saute");
        if(out){
            System.out.println("Successful test!");
        }
        else{
            System.out.println("ERRORERRORERROR\nERRORERRORERROR\nERRORERRORERROR\n");
        }
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
