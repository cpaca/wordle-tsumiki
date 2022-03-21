package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args){
        System.out.printf("Maximum value LONG:  %,d\n", Long.MAX_VALUE);
        System.out.printf("Current time:        %,d\n", System.nanoTime());
        WordleGuesser wordle = new WordleGuesser(GetWords());
        char[] word;
        long quality;

        word = wordle.FindBestGuess();
        quality = wordle.QualifyGuess(word);
        System.out.print("Best guess: ");
        System.out.println(word);
        System.out.println("Quality: " + quality + "\n");

        wordle.ApplyGuess("track".toCharArray(), new byte[]{0,0,1,0,0});
        word = wordle.FindBestGuess();
        quality = wordle.QualifyGuess(word);
        System.out.print("Best guess: ");
        System.out.println(word);
        System.out.println("Quality: " + quality + "\n");

        wordle.ApplyGuess("among".toCharArray(), new byte[]{2,0,1,0,0});
        word = wordle.FindBestGuess();
        quality = wordle.QualifyGuess(word);
        System.out.print("Best guess: ");
        System.out.println(word);
        System.out.println("Quality: " + quality + "\n");

        wordle.ApplyGuess("lapse".toCharArray(), new byte[]{1,1,0,0,0});
        word = wordle.FindBestGuess();
        quality = wordle.QualifyGuess(word);
        System.out.print("Best guess: ");
        System.out.println(word);
        System.out.println("Quality: " + quality + "\n");
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
