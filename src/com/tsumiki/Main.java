package com.tsumiki;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args){
        Tests.RunTests();

        TimeQualifyGuess();

        for(byte a = 0; a < 3; a++){
            for(byte b = 0; b < 3; b++){
                for(byte c = 0; c < 3; c++){
                    for(byte d = 0; d < 3; d++){
                        for(byte e = 0; e < 3; e++){
                            byte[] state = new byte[]{a,b,c,d,e};
                            int stateBase3 = a*81 + b*27 + c*9 + d*3 +e;
                            WordleGuesser wordle = new WordleGuesser(GetWords());
                            wordle.ApplyGuess("adieu".toCharArray(), state);
                            int size = wordle.getAnswers().size();

                            System.out.println("State " + stateBase3 + ", remaining words: " + size);
                        }
                    }
                }
            }
        }
        WordleGuesser wordle = new WordleGuesser(GetWords());
        wordle.QualifyGuess("adieu".toCharArray());
    }

    public static final int iters = 100000;
    public static void TimeQualifyGuess(){
        long totalTime = 0;
        WordleGuesser wordle = new WordleGuesser(GetWords());
        DecimalFormat format = new DecimalFormat("0,000,000,000");
        Random r = new Random(System.nanoTime());
        char[][] words = GetWords();
        for(int i = 0; i < iters; i++){
            char[] word = words[r.nextInt(words.length)];
            totalTime -= System.nanoTime();
            wordle.ApplyGuess("pryse".toCharArray(), new byte[]{0,0,0,1,1});
            totalTime += System.nanoTime();
        }
        System.out.println("Long.MAX_VALUE: " + format.format(Long.MAX_VALUE));
        System.out.println("Total time:     " + format.format(totalTime));
        System.out.println("Average time:   " + format.format(totalTime/iters));
    }

    public static char[][] GetWords(){
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

    public static void PrintIntArray(byte[] arr){
        System.out.print("[");
        for(int i = 0; i < arr.length; i++){
            System.out.print(arr[i]);
            if(i < arr.length-1){
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
