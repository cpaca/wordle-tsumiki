package com.tsumiki;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args){
        Tests.TestOne();

        MultiWordleGuesser quordle = new MultiWordleGuesser(4, GetWords());

        quordle.ApplyGuess("lares".toCharArray(), new byte[][]{
                new byte[] {1,0,0,0,0},
                new byte[] {0,0,1,1,0},
                new byte[] {0,1,0,0,2},
                new byte[] {0,0,0,1,1},
        });

        quordle.ApplyGuess("colly".toCharArray(), new byte[][]{
                new byte[] {0,0,1,0,0},
                new byte[] {2,1,0,0,0},
                new byte[] {0,0,0,0,0},
                new byte[] {0,1,0,0,0},
        });

        quordle.ApplyGuess("pling".toCharArray(), new byte[][]{
                new byte[] {0,2,0,0,0},
                new byte[] {0,0,0,2,0},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,0,2,0},
        });

        quordle.ApplyGuess("crone".toCharArray(), new byte[][]{
                new byte[] {0,0,0,0,0},
                new byte[] {2,2,2,2,2},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,2,2,2},
        });

        quordle.ApplyGuess("bluff".toCharArray(), new byte[][]{
                new byte[] {0,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,0,0,0},
        });

        quordle.ApplyGuess("fluff".toCharArray(), new byte[][]{
                new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,0,0,0},
        });

        quordle.ApplyGuess("shone".toCharArray(), new byte[][]{
                // new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {1,0,0,0,0},
                new byte[] {2,0,2,2,2},
        });

        quordle.ApplyGuess("stone".toCharArray(), new byte[][]{
                // new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {1,0,0,0,0},
                new byte[] {2,2,2,2,2},
        });

        quordle.ApplyGuess("amass".toCharArray(), new byte[][]{
                // new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {2,2,2,2,2},
                // new byte[] {2,0,2,2,2},
        });

        char[] word = quordle.FindBestGuess();
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
