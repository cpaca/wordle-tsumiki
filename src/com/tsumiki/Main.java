package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        WordleGuesser wordle = new WordleGuesser(GetWords());
        System.out.println(wordle.getAnswers().size());

        MarkTime();
        wordle.ApplyGuess("adieu".toCharArray(),new byte[]{1,0,0,1,1});
        MarkTime();

        System.out.println(wordle.getAnswers().size());
        wordle.ApplyGuess("snort".toCharArray(),new byte[]{2,0,0,0,1});
        System.out.println(wordle.getAnswers().size());
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

    // Marks the time since the last time this function was called.
    // And then prints it.
    // Doesn't do anything if this is the first call.
    private static long lastMark = 0;
    private static void MarkTime(){
        if(lastMark == 0){
            lastMark = System.nanoTime();
        }
        else{
            long nextMark = System.nanoTime();
            System.out.printf("Time: %,d\n", nextMark-lastMark);
            lastMark = nextMark;
        }
    }
}
