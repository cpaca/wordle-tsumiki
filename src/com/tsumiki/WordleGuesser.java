package com.tsumiki;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// This class takes a list of valid guesses and answers
// And takes a guess as to which one is the best one.
public class WordleGuesser {

    public static final int WORDLEN = 5;
    private static final int WORDLEN_SQ = WORDLEN * WORDLEN;

    private char[][] _guesses;
    private List<char[]> _answers;

    public WordleGuesser(char[][] guesses){
        this(guesses, guesses);
    }

    public WordleGuesser(char[][] guesses, char[][] answers){
        _guesses = new char[guesses.length][WORDLEN];
        for(int i = 0; i < guesses.length; i++){
            char[] word = guesses[i];

            if(word.length != WORDLEN){
                throw new IllegalArgumentException();
            }

            System.arraycopy(word, 0, _guesses[i], 0, WORDLEN);
        }

        // Note:
        // While LinkedList is O(n) time for adding things
        // If we do it like this, it's still O(n) time, despite adding n elements
        // However, removing elements is O(1) time.
        // As opposed to ArrayList, where removing elements is O(n) time.
        //
        //
        //
        // Update: ArrayList might be better here; if we create a new list instead of modifying the existing one
        // then destroying the existing one when we need to modify it
        // it might be faster
        _answers = new ArrayList<>(Arrays.asList(answers));
    }

    // A note about states:
    // State 0 represents "This letter is not here."
    // State 1 represents "This letter is here, but not in this position."
    // State 2 represents "This letter is in this word."
    public void ApplyGuess(final char[] inGuess, final byte[] inState){
        System.out.println(inGuess);
        if(inGuess.length != WORDLEN || inState.length != WORDLEN){
            return; // Invalid input.
        }

        char[] guess = new char[WORDLEN];
        byte[] state = new byte[WORDLEN];
        System.arraycopy(inGuess, 0, guess, 0, WORDLEN);
        System.arraycopy(inState, 0, state, 0, WORDLEN);

        // Handle the two-states.
        // This is faster than handling the one-states and zero-states, so it's done first.
        ArrayList<char[]> twoStateFilteredAnswers = new ArrayList<>();
        for(char[] word:_answers){
            boolean validWord = true;
            for(int i = 0; i < WORDLEN; i++){
                // Note: The following comment is simplified into one if statement.
                // Run the truth table on your own.
                /*
                // First, handle the state=2 case:
                if( (state[i]==2) && (guess[i] == word[i]) ) {valid index}
                // Then handle the non state = 2 case:
                if( (state[i]!=2) && (guess[i] != word[i]) ) {valid index}
                 */
                if( (state[i]==2) != (guess[i] == word[i])){
                    validWord = false;
                    break; // word is invalid, speed things up with this
                }
            }

            if(validWord){
                twoStateFilteredAnswers.add(word);
            }
        }

        List<char[]> oneStateFilteredAnswers = new ArrayList<>();
        // Handle the 1-states and 0-states.
        for(char[] word : twoStateFilteredAnswers){
            byte[] theorstate = new byte[WORDLEN];
            for(int h = 0; h < WORDLEN_SQ; h++){
                int i = h/WORDLEN;
                int j = h%WORDLEN;
                // This letter already got handled by the two-processor
                if(state[i] == 2){
                    continue;
                }

                // Already got processed by the one-processor.
                if(theorstate[j] != 0){
                    continue;
                }

                // If the letters are the same in different positions...
                if(guess[i] == word[j]){
                    // It's a one-proc.
                    theorstate[i] = 1;
                }
            }

            boolean validWord = true;
            for(int i = 0; i < WORDLEN; i++){
                // Note:
                // Mod 2 is done because theorstate only has states 0 and 1
                // But state has states 0, 1, and 2. (theorstate uses 0s instead of 2s.)
                // So I need to turn the 0s into 2s, hence, %2.
                if(theorstate[i] != (state[i]%2)){
                    // Invalid word
                    validWord = false;
                    break;
                }
            }

            if(validWord){
                oneStateFilteredAnswers.add(word);
            }
        }

        // just... ignore the fact that this drops the existing _answers
        // GC will handle picking that up
        _answers = oneStateFilteredAnswers;
    }

    public List<char[]> getAnswers() {
        return _answers;
    }
}
