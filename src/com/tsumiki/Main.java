package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args){
        Tests.TestOne();

        WordleGuesser wordle = new WordleGuesser(GetWords());
        wordle.ApplyGuess("lares".toCharArray(), new byte[]{0,0,2,0,0});
        wordle.ApplyGuess("sabra".toCharArray(), new byte[]{0,0,0,2,0});
        wordle.ApplyGuess("sapor".toCharArray(), new byte[]{0,0,0,0,1});
        wordle.ApplyGuess("savor".toCharArray(), new byte[]{0,0,0,0,1});
        wordle.ApplyGuess("curry".toCharArray(), new byte[]{0,2,2,2,2});
        wordle.ApplyGuess("aahed".toCharArray(), new byte[]{0,0,0,0,0});
        wordle.ApplyGuess("gurry".toCharArray(), new byte[]{0,2,2,2,2});
        wordle.ApplyGuess("murry".toCharArray(), new byte[]{0,2,2,2,2});

        char[] word = wordle.FindBestGuess();
        System.out.println(word);
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

    public static boolean EqualCharArrays(char[] a, char[] b){
        if(a.length != b.length){
            return false;
        }
        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i])
                return false;
        }
        return true;
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
