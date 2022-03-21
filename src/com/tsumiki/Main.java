package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args){
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
