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
        _answers = new LinkedList<>(Arrays.asList(answers));
    }

    // A note about states:
    // State 0 represents "This letter is not here."
    // State 1 represents "This letter is here, but not in this position."
    // State 2 represents "This letter is in this word."
    public void MakeGuess(char[] inGuess, byte[] inState){
        if(inGuess.length != WORDLEN || inState.length != WORDLEN){
            return; // Invalid input.
        }

        char[] guess = new char[WORDLEN];
        byte[] state = new byte[WORDLEN];
        System.arraycopy(inGuess, 0, guess, 0, WORDLEN);
        System.arraycopy(inState, 0, state, 0, WORDLEN);

        // Handle the two-situations.
        ArrayList<char[]> toRemove = new ArrayList<>();
        for(char[] word:_answers){
            for(int i = 0; i < WORDLEN; i++){
                // Note: The following comment is simplified into one if statement.
                // Run the truth table on your own.
                /*
                // First, handle the state=2 case:
                if( (state[i]==2) && (guess[i] == word[i]) ) {code}
                // Then handle the non state = 2 case:
                if( (state[i]!=2) && (guess[i] != word[i]) ) {code}
                 */
                if( (state[i]==2) == (guess[i] == word[i]) ){
                    // The word is fine.
                } else{
                    // Remove the word because it's invalid.
                    toRemove.add(word);
                    break; // breaks through only one level, not all of them.
                }
            }
        }
        
        for(char[] wordToRemove:toRemove){
            _answers.remove(wordToRemove);
        }
    }

    public List<char[]> getAnswers() {
        return _answers;
    }
}
