package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;

public class Main {

    public static void main(String[] args){
        boolean PassTests = ConfirmProtoGuess();
        if(!PassTests){
            System.out.println("Prototype function is not identical.");
        }
        else{
            System.out.println("Prototype function passes");
        }

        TestApplyGuessSpeed();
        TestProtoGuessSpeed();
    }

    private static final int iters = 1000;
    private static void TestApplyGuessSpeed(){
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

    private static void TestProtoGuessSpeed(){
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

    private static boolean ConfirmProtoGuess(){
        BiPredicate<String, byte[]> cons = (String s, byte[] b) -> {
            WordleGuesser wordle1 = new WordleGuesser(GetWords());
            WordleGuesser wordle2 = new WordleGuesser(GetWords());

            wordle1.ApplyGuess(s.toCharArray(), b);
            wordle2.ApplyGuess(s.toCharArray(), b);

            boolean out = wordle1.getAnswers().size() == wordle2.getAnswers().size();
            if(!out){
                System.out.println("Test failed: " + s);
            }
            return out;
        };

        boolean out = cons.test("adieu", new byte[] {1,0,0,1,1});
        out = out && cons.test("among", new byte[] {0,0,0,1,0});
        out = out && cons.test("nicer", new byte[] {1,0,0,2,1});
        out = out && cons.test("rebel", new byte[] {2,2,0,2,0});
        out = out && cons.test("renew", new byte[] {2,2,2,2,2});

        // Using non-words now just to test edge cases.
        out = out && cons.test("anney", new byte[] {1,1,2,0,2});

        return out;
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
