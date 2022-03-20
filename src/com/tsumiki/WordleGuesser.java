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

    // Prototype Apply Guess.
    // If this is faster, it becomes ApplyGuess.
    // If not, it dies.
    public void ProtoApplyGuess(final char[] inGuess, final byte[] inState){
        if(inGuess.length != WORDLEN || inState.length != WORDLEN){
            return; // Invalid input.
        }

        char[] guess = new char[WORDLEN];
        byte[] state = new byte[WORDLEN];
        System.arraycopy(inGuess, 0, guess, 0, WORDLEN);
        System.arraycopy(inState, 0, state, 0, WORDLEN);

        List<char[]> filteredWords = new ArrayList<>();
        for(char[] word : _answers){
            boolean validWord = true;
            byte[] theorstate = new byte[WORDLEN];
            for(int i = 0; i < WORDLEN; i++){
                if( (state[i] == 2) && (guess[i] == word[i]) ){
                    // State=2 case, and the word is valid.
                    // This index is valid.
                }
                // Then handle the state != 2 case:
                else if( (state[i] != 2) && (guess[i] != word[i]) ){
                    // State is NOT 2 case, but it does pass the state=2 checks
                    // But we still need to run the state = 0 and 1 case.

                    // Next, calculate the "theoretic state"
                    // which is like the actual state, but 2s are 0s instead
                    // This is helpful because it allows us to only look at the 1-cases
                    // Fortunately, Java initializes arrays to be full of 0s.
                    for(int j = 0; j < WORDLEN; j++){
                        // Already processed by the one-state
                        // Ignore
                        if(theorstate[j] != 0){
                            continue;
                        }

                        // Note about the I=J case:
                        // If guess[i] == word[j] AND I=J
                        // Then guess[i] == word[i]
                        // If you recall the if statement a few lines above that enters this for loop
                        // guess[i] could never equal word[i], or else it wouldn't enter this for loop.

                        if (guess[i] == word[j]) {

                            // THOUGHTS FOR FUTURE SELF:
                            // Instead of using theorstate[i] = 1
                            // What if you used if(state[i] != 1){validWord = false;}
                            //
                            // Issue to consider:
                            // What about the 1-states in the state array that do NOT get considered?
                            // What to do about those?
                            //
                            // Possible answer to solution:
                            //      Keep theorstate for the above if statement, but use the new code to speed things up
                            //
                            //      Use state, and set it to 5 to represent "this 1-state has been checked"
                            //      Setting to 5 instead of 3 or 4 means a % 4 operation can be done
                            //      Which is computationally VERY FAST
                            //      If the compiler doesn't make it very fast
                            //      Just do a num && 0x03 operation, or num && 0b00...0011

                            theorstate[i] = 1;

                            // Holy shit. IntelliJ with the MASSIVE carry.
                            // Hadn't even considered this before.
                            break;
                        }
                    }
                }
                else{
                    // Fails the state = 2 check.
                    validWord = false;
                    break;
                }
            }

            if(validWord){
                // Passed all the two-states
                // Now need to 100% confirm the one-states
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
            }

            if(validWord){
                // Passed both the 2-checks and the 01-checks
                // This is a valid word!
                filteredWords.add(word);
            }
        }

        // just... ignore the fact that this drops the existing _answers
        // GC will handle picking that up
        _answers = filteredWords;
    }

    public List<char[]> getAnswers() {
        return _answers;
    }
}
