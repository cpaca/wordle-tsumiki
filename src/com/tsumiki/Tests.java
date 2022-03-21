package com.tsumiki;

import java.util.function.BiConsumer;

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

        BiConsumer<String, String[]> tester = (String word, String[] states) -> {
            assert EqualArrays(quordle.FindBestGuess(), word);
            quordle.ApplyGuess(word.toCharArray(), states);
        };

        tester.accept("lares", new String[]{
                "00001",
                "00200",
                "01000",
                "01001",
        });

        tester.accept("sooty", new String[]{
                "12010",
                "02020",
                "00000",
                "20000",
        });

        tester.accept("spank", new String[]{
                "10000",
                "00000",
                "00200",
                "22222",
        });

        tester.accept("bhaji", new String[]{
                "00001",
                "01000",
                "02200",
                //"22222",
        });

        tester.accept("chaff", new String[]{
                "00010",
                "01000",
                "22222",
                //"22222",
        });

        tester.accept("worth", new String[]{
                "02010",
                "22222",
                //"22222",
                //"22222",
        });

        tester.accept("foist", new String[]{
                "22222",
                //"22222",
                //"22222",
                //"22222",
        });
    }

    private static void TestThree(){
        assert EqualArrays(WordleGuesser.ToStateArray("01210"), new byte[]{0,1,2,1,0});
        assert EqualArrays(WordleGuesser.ToStateArray("00000"), new byte[]{0,0,0,0,0});
        assert EqualArrays(WordleGuesser.ToStateArray("11111"), new byte[]{1,1,1,1,1});
        assert EqualArrays(WordleGuesser.ToStateArray("22222"), new byte[]{2,2,2,2,2});
        assert EqualArrays(WordleGuesser.ToStateArray("21010"), new byte[]{2,1,0,1,0});
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
