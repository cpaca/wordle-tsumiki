package com.tsumiki;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.tsumiki.PythonTests.RunPythonTests;

public class Tests {

    public static void RunTests(){
        RunPythonTests();

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
        Consumer<String> cons = (String s) -> {
            WordleGuesser wordle = new WordleGuesser(Main.GetWords());
            long theorQual = wordle.QualifyGuess(s.toCharArray());

            int[] states = new int[3*3*3*3*3];
            byte[] state = {0, 0, 0, 0, 0};
            int stateBase3 = 0;
            int index = 0;

            while(index < state.length){
                wordle = new WordleGuesser(Main.GetWords());
                wordle.ApplyGuess(s.toCharArray(), state);
                states[stateBase3] = wordle.getAnswers().size();

                index = 0;
                stateBase3++;
                while(index < state.length && state[index] == 2){
                    state[index] = 0;
                    index++;
                }
                if(index < state.length){
                    state[index]++;
                }
            }

            long actualQual = 0;
            for(int i = 0; i < states.length-1; i++){
                actualQual += states[i] * states[i];
            }
            assert actualQual == theorQual;
        };

        cons.accept("adieu");
        cons.accept("snort");
        cons.accept("frays");
        cons.accept("tests");
        cons.accept("zzzzz");
        cons.accept("lares");
        cons.accept("meows");
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
