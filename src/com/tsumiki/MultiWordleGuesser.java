package com.tsumiki;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MultiWordleGuesser {

    private List<WordleGuesser> wordles;
    private boolean DefaultFirstGuess = false;

    public MultiWordleGuesser(int NumWordles, char[][] guesses){
        this(NumWordles, guesses, guesses);
    }

    public MultiWordleGuesser(int NumWordles, char[][] guesses, char[][] answers){
        wordles = new ArrayList<>();
        for(int i = 0; i < NumWordles; i++){
            wordles.add(new WordleGuesser(guesses, answers));
        }

        if(answers.length > 10000){
            DefaultFirstGuess = true;
        }
    }

    public void ApplyGuess(final char[] guess, final byte[][] states){
        if(states.length != wordles.size()){
            throw new IllegalArgumentException();
        }
        int stateIndex = 0;
        for(int i = 0; i < wordles.size(); i++){
            wordles.get(i).ApplyGuess(guess, states[stateIndex]);
            int size = wordles.get(i).getAnswers().size();
            if (size == 0) {
                throw new IllegalStateException();
            }
            else if(size == 1){
                // only remove this one if our guess is actually the answer
                if(EqualCharArrays(wordles.get(i).getAnswers().get(0), guess)){
                    wordles.remove(i);
                    i--;
                }
            }
            stateIndex++;
        }

        DefaultFirstGuess = false;
    }

    public void ApplyGuess(final char[] guess, final String[] stringStates){
        byte[][] states = new byte[stringStates.length][WordleGuesser.WORDLEN];
        for(int i = 0; i < stringStates.length; i++){
            states[i] = WordleGuesser.ToStateArray(stringStates[i]);
        }
        ApplyGuess(guess, states);
    }

    public long QualifyGuess(final char[] guess){
        long out = 0;
        for(WordleGuesser wordle : wordles){
            // Note: We need a size-adjusted quality
            // So gaining more information is designated as a "higher quality"
            long quality = Long.MAX_VALUE;

            long worstQual = wordle.getAnswers().size();
            worstQual *= worstQual;
            quality /= worstQual;

            long thisQual = wordle.QualifyGuess(guess);
            if(thisQual == 0){
                // Perfect quality.
                // Use it.
                return 0;
            }
            quality *= thisQual;
            out += quality;

            // Integer overflow
            if(out < 0){
                return Long.MAX_VALUE;
            }
        }
        return out;
    }

    // Note: This function will not always return the best guess.
    // There may be some guesses which would be even better than this one.
    // However, for the sake of this function not taking a lot of time per call,
    // this implementation is used.
    public char[] FindBestGuess(){
        if(wordles.size() == 0){
            return "lares".toCharArray(); // whatever
        }

        if(DefaultFirstGuess){
            DefaultFirstGuess = false;
            return "lares".toCharArray();
        }

        // First, find the four best guesses:
        char[][] bestGuesses = new char[wordles.size()][WordleGuesser.WORDLEN];

        for(int i = 0; i < bestGuesses.length; i++){
            bestGuesses[i] = wordles.get(i).FindBestGuess();
        }

        // Next, find the quality of each best-guess
        long[] qualities = new long[bestGuesses.length];
        for(int i = 0; i < qualities.length; i++){
            qualities[i] = QualifyGuess(bestGuesses[i]);
        }

        // Next, find the minimum one
        long minQual = qualities[0];
        int minIndex = 0;
        for(int i = 1; i < qualities.length; i++){
            long quality = qualities[i];
            if(quality < minQual){
                minIndex = i;
                minQual = quality;
            }
        }

        // Finally, we know the best word:
        return bestGuesses[minIndex];
    }

    private static boolean EqualCharArrays(char[] a, char[] b){
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
