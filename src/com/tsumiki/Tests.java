package com.tsumiki;

import javafx.beans.value.WritableObjectValue;

import java.util.Random;

import static com.tsumiki.WordleGuesser.WORDLEN;

public class Tests {

    public static void RunTests(){
        TestOne();
        TestTwo();
        TestThree();
    }

    private static void TestOne(){
        WordleGuesser wordle = new WordleGuesser(Main.GetWords());

        assert wordle.getAnswers().size() == 12972;

        wordle.ApplyGuess("track".toCharArray(), new byte[]{0,0,1,0,0});
        assert wordle.getAnswers().size() == 1647;

        wordle.ApplyGuess("among".toCharArray(), new byte[]{2,0,1,0,0});
        assert wordle.getAnswers().size() == 16;

        wordle.ApplyGuess("lapse".toCharArray(), new byte[]{1,1,0,0,0});
        assert wordle.getAnswers().size() == 6;
    }

    private static void TestTwo(){
        MultiWordleGuesser quordle = new MultiWordleGuesser(4, Main.GetWords());

        assert EqualArrays(quordle.FindBestGuess(), "lares");
        quordle.ApplyGuess("lares".toCharArray(), new byte[][]{
                new byte[] {1,0,0,0,0},
                new byte[] {0,0,1,1,0},
                new byte[] {0,1,0,0,2},
                new byte[] {0,0,0,1,1},
        });

        assert EqualArrays(quordle.FindBestGuess(), "colly");
        quordle.ApplyGuess("colly".toCharArray(), new byte[][]{
                new byte[] {0,0,1,0,0},
                new byte[] {2,1,0,0,0},
                new byte[] {0,0,0,0,0},
                new byte[] {0,1,0,0,0},
        });

        assert EqualArrays(quordle.FindBestGuess(), "pling");
        quordle.ApplyGuess("pling".toCharArray(), new byte[][]{
                new byte[] {0,2,0,0,0},
                new byte[] {0,0,0,2,0},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,0,2,0},
        });

        assert EqualArrays(quordle.FindBestGuess(), "crone");
        quordle.ApplyGuess("crone".toCharArray(), new byte[][]{
                new byte[] {0,0,0,0,0},
                new byte[] {2,2,2,2,2},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,2,2,2},
        });

        assert EqualArrays(quordle.FindBestGuess(), "bluff");
        quordle.ApplyGuess("bluff".toCharArray(), new byte[][]{
                new byte[] {0,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,0,0,0},
        });

        assert EqualArrays(quordle.FindBestGuess(), "fluff");
        quordle.ApplyGuess("fluff".toCharArray(), new byte[][]{
                new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,0,0,0},
        });

        assert EqualArrays(quordle.FindBestGuess(), "shone");
        quordle.ApplyGuess("shone".toCharArray(), new byte[][]{
                // new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {1,0,0,0,0},
                new byte[] {2,0,2,2,2},
        });

        assert EqualArrays(quordle.FindBestGuess(), "stone");
        quordle.ApplyGuess("stone".toCharArray(), new byte[][]{
                // new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {1,0,0,0,0},
                new byte[] {2,2,2,2,2},
        });

        assert EqualArrays(quordle.FindBestGuess(), "amass");
        quordle.ApplyGuess("amass".toCharArray(), new byte[][]{
                // new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {2,2,2,2,2},
                // new byte[] {2,0,2,2,2},
        });
    }

    private static void TestThree(){
        assert EqualArrays(WordleGuesser.LongToBytes(901210), new byte[]{0,1,2,1,0});
        assert EqualArrays(WordleGuesser.LongToBytes(900000), new byte[]{0,0,0,0,0});
        assert EqualArrays(WordleGuesser.LongToBytes(911111), new byte[]{1,1,1,1,1});
        assert EqualArrays(WordleGuesser.LongToBytes(922222), new byte[]{2,2,2,2,2});
        assert EqualArrays(WordleGuesser.LongToBytes(921010), new byte[]{2,1,0,1,0});
    }

    private static boolean EqualArrays(char[] a, String b){
        return EqualArrays(a, b.toCharArray());
    }
    private static boolean EqualArrays(String a, char[] b){
        return EqualArrays(a.toCharArray(), b);
    }
    private static boolean EqualArrays(char[] a, char[] b){
        if(a.length != b.length){
            return false;
        }
        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i])
                return false;
        }
        return true;
    }
    private static boolean EqualArrays(byte[] a, byte[] b){
        if(a.length != b.length){
            return false;
        }
        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i])
                return false;
        }
        return true;
    }
}
