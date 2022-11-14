package com.tsumiki;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static int GuessNumber = 1;

    private static WordleGuesser globalWordle = null;

    public static void main(String[] args){
        // Tests.RunTests();
        // GuessInformation("audio", "10010");
        // GuessInformation("wails", "01100");

    }

    public static void GuessInformation(String guess, String colors){
        if(globalWordle == null){
            globalWordle = new WordleGuesser(GetWords());
        }
        GuessInformation(globalWordle, guess, colors);
    }

    public static void GuessInformation(WordleGuesser wordle, String guess, String colors){
        wordle.ApplyGuess(guess, colors);

        char[] word = wordle.FindBestGuess();
        List<char[]> remainingAnswers = wordle.getAnswers();

        System.out.println("After guess #" + GuessNumber + ", which is ||" + guess + "||:");
        System.out.println("AI requests ||" + new String(word) + "|| be played next.");
        System.out.println("AI states there are ||" + remainingAnswers.size() + "|| answers remaining.");
        if(remainingAnswers.size() < 10){
            StringBuilder answers = new StringBuilder();
            for(int i = 0; i < remainingAnswers.size(); i++){
                if(i != 0){
                    answers.append(", ");
                }
                answers.append(new String(remainingAnswers.get(i)));
            }

            System.out.println("||Remaining possible answers: " + answers + "||");
        }
        System.out.println();

        GuessNumber++;
    }

    public static final int iters = 10000;
    public static void TimeQualifyGuess(){
        long totalTime = 0;
        WordleGuesser wordle = new WordleGuesser(GetWords());
        DecimalFormat format = new DecimalFormat("0,000,000,000");
        Random r = new Random(System.nanoTime());
        char[][] words = GetWords();
        for(int i = 0; i < iters; i++){
            char[] word = words[r.nextInt(words.length)];
            totalTime -= System.nanoTime();
            wordle.QualifyGuess(word);
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
}
