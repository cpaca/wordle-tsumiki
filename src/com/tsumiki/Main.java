package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args){
        Tests.RunTests();

        System.out.println("\n\n\n\n\n\n");
        MultiWordleGuesser quordle = new MultiWordleGuesser(4, GetWords());

        quordle.ApplyGuess("lares".toCharArray(), new String[]{
                "02010",
                "00010",
                "11000",
                "22020",
        });

        quordle.ApplyGuess("mange".toCharArray(), new String[]{
                "02022",
                "00121",
                "01000",
                "02101",
        });

        quordle.ApplyGuess("cadge".toCharArray(), new String[]{
                "02222",
                "00121",
                "11000",
                "02201",
        });

        quordle.ApplyGuess("deign".toCharArray(), new String[]{
                "11020",
                "22222",
                "00000",
                "11002",
        });

        quordle.ApplyGuess("fadge".toCharArray(), new String[]{
                "02222",
                //"22222",
                "01000",
                "02201",
        });

        quordle.ApplyGuess("laden".toCharArray(), new String[]{
                "02210",
                //"22222",
                "11000",
                "22222",
        });

        quordle.ApplyGuess("octal".toCharArray(), new String[]{
                "00010",
                //"22222",
                "01011",
                //"22222",
        });

        quordle.ApplyGuess("gadge".toCharArray(), new String[]{
                "02222",
                //"22222",
                "01000",
                //"22222",
        });

        char[] word = quordle.FindBestGuess();
        System.out.print("Next word: ");
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
