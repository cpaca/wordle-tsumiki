package com.tsumiki;

public class Tests {
    public static void TestOne(){
        WordleGuesser wordle = new WordleGuesser(Main.GetWords());
        char[] word;
        long quality;

        assert wordle.getAnswers().size() == 12972;

        wordle.ApplyGuess("track".toCharArray(), new byte[]{0,0,1,0,0});
        assert wordle.getAnswers().size() == 1647;

        wordle.ApplyGuess("among".toCharArray(), new byte[]{2,0,1,0,0});
        assert wordle.getAnswers().size() == 16;

        wordle.ApplyGuess("lapse".toCharArray(), new byte[]{1,1,0,0,0});
        assert wordle.getAnswers().size() == 6;
    }
}
