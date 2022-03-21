package com.tsumiki;

import java.util.ArrayList;
import java.util.List;

public class Tests {

    public static List<Integer> failedTests = new ArrayList<>();

    public static void TestOne(){
        WordleGuesser wordle = new WordleGuesser(Main.GetWords());
        char[] word;
        long quality;
        System.out.println("NEXT TEST");
        System.out.println("NEXT TEST");
        System.out.println("NEXT TEST");
        System.out.println("NEXT TEST");
        System.out.println("NEXT TEST");

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

    public static void PrintFailedTests(){
        for(Integer i : failedTests){
            System.out.println("Failed test: " + i);
        }
        if(failedTests.size() == 0){
            System.out.println("No failed tests.");
        }
    }
}
