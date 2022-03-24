package com.tsumiki;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This class takes a list of valid guesses and answers
// And takes a guess as to which one is the best one.
public class WordleGuesser {

    // WARNING:
    // WARNING:
    // This function is memory intensive with respect to WORDLEN
    // In fact, it takes - at least - WORDLEN * 3^WORDLEN bytes to run QualifyGuess
    // While other functions are better than that, it is SITLL VERY IMPORTANT TO KNOW!
    // For this reason
    // (And also because the code breaks...)
    // WORDLEN is capped at 16.
    // Beyond 16, this class has undefined behavior.
    public static final int WORDLEN = 5;

    // 3 to the power of WORDLEN
    // If this was a long that'd imply an array with over 2 billion elements
    // Yeah, no.
    // Besides, as long as the words are kept under 19 letters long
    // this won't cause issues.
    private static int THREE_POW_WORDLEN = 1;

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
            for(char c : word){
                if(c < 'a' || c > 'z'){
                    throw new IllegalArgumentException("WordleGuesser only accepts lowercase letters as input.");
                }
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

        // Init 3^WORDLEN
        if(THREE_POW_WORDLEN == 1){
            for(int i = 0; i < WORDLEN; i++){
                long prev = THREE_POW_WORDLEN;
                THREE_POW_WORDLEN *= 3;

                // Note: The IDE will say "This is always true!"
                // It's not. Remember, integer overflows exist.
                if(THREE_POW_WORDLEN < prev){
                    throw new IllegalStateException("The word length is too big, please choose a smaller one.");
                }
            }
        }
    }

    // A note about states:
    // State 0 represents "This letter is not here."
    // State 1 represents "This letter is here, but not in this position."
    // State 2 represents "This letter is in this word."

    /**
     *
     * @param inGuess - The guess you've made
     * @param inState - The state, with blacks (not found) represented with 0, yellows (found, wrong position)
     *                represented with a 1, and greens (found, right position) represented with a two
     */
    public void ApplyGuess(final char[] inGuess, final byte[] inState){
        if(inGuess.length != WORDLEN || inState.length != WORDLEN){
            return; // Invalid input.
        }

        for(char c : inGuess){
            if(c < 'a' || c > 'z'){
                throw new IllegalArgumentException("WordleGuesser only accepts lowercase letters as input");
            }
        }

        char[] guess = new char[WORDLEN];
        byte[] state = new byte[WORDLEN];
        System.arraycopy(inGuess, 0, guess, 0, WORDLEN);
        System.arraycopy(inState, 0, state, 0, WORDLEN);

        List<char[]> filteredWords = new ArrayList<>();
        int[] OneIndices = new int[26]; // indices of the one-states
        for(char[] word : _answers){
            boolean validWord = true;
            Arrays.fill(OneIndices, 0);

            // note that a is 0, b is 1, etc.
            for(int i = WORDLEN-1; i >= 0; i--){
                if( (state[i] == 2) && (word[i] == guess[i]) ){
                    // 2-state
                    // And the letter matches
                    // Move on
                }
                else if( (state[i] != 2) && (word[i] != guess[i]) ){
                    // Not a 2-state and the letters don't match
                    // Save the index for the one-state

                    // Count how many times each letter appears
                    int letter = word[i] - 'a';
                    OneIndices[letter]++;
                }
                else{
                    // Either it's a 2-state and the letters don't match
                    // Or it's not a 2-state and the letters match
                    // Either way it's an invalid word
                    validWord = false;
                    break;
                }
            }

            // Run the 1-states
            if(validWord){
                for(int i = 0; i < WORDLEN; i++){
                    if(state[i] == 2){
                        continue;
                    }
                    int letter = guess[i] - 'a';
                    if(OneIndices[letter] != 0){
                        // Letter found, but not at position i
                        // So this is a 1-index
                        OneIndices[letter]--;
                        if(state[i] == 0){
                            // Expected a 0-index, got a 1-index
                            // Invalid
                            validWord = false;
                            break;
                        }
                    }
                    else{
                        // Letter not found anywhere in the wrod
                        // So this is a 0-index
                        if(state[i] == 1){
                            // Expected a 1-index, got a 0-index
                            // Invalid
                            validWord = false;
                            break;
                        }
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

    // Returns the quality of a guess
    // Note, this is like golf, so you want this to return as SMALL of a value as possible.
    // If the input is invalid, this returns the worst possible quality.
    public long QualifyGuess(final char[] inGuess){
        if(inGuess.length != WORDLEN){
            return Long.MAX_VALUE; // Invalid input.
        }

        char[] guess = new char[WORDLEN];
        System.arraycopy(inGuess, 0, guess, 0, WORDLEN);

        // Lots of for loops.
        // An iterating variable ends up being needed to speed up executions.
        // I doubt it matters much
        // but it probably saves some time.
        int i;

        // Note about how states are stored in this:
        // State {0, 1, 2, 1, 0} can be represented as 01210
        // State {1, 2, 2, 1, 0} can be represented as 12210
        // etc.
        // Note that all of these representations can be done base-3
        // So 01210 can be represented in base 3
        // as (3^4)*0 + (3^3)*1 + (3^2)*2 + (3^1)*1 + (3^0)*0
        // And state 12210 can be represented in base 3
        // as (3^4)*1 + (3^3)*2 + (3^2)*2 + (3^1)*1 + (3^0)*0
        int[] states = new int[THREE_POW_WORDLEN];

        // State, with a weird representaation (using booleans)
        // 2s are represented with TRUEs
        // 0s and 1s are represented with FALSEs
        // When 0s and 1s are calculated, they're immediately added to stateBase3
        // so that's why we can use booleans instead of bytes here
        boolean[] state = new boolean[WORDLEN];

        // How many times the letter has appeared in the word
        // a is 'a'-'a'=0, b is 'b'-'a' or 1, etc.
        byte[] finds = new byte[26];

        // Actually, you only need to reset letters which appear in the guess.
        // You can totally ignore how often letters which don't appear in the guess occur.
        // Note, this has a side effect:
        // finds[] for values NOT in the guess will be affected by what words came before.
        // And I'm fine with that.
        int[] resets = new int[WORDLEN];
        for(i = 0; i < WORDLEN; i++){
            int letter = guess[i] - 'a';
            resets[i] = letter;
        }

        for(char[] word : _answers){
            // For the first loop, state is initialized to a bunch of 0s
            // There's a thing later on that resets state[i].
            // It's right when it calculates the base-3 representation.

            // We do need to reset the letters though.
            // And given that we only find - at most - 5 characters
            // filling the entire thing with 0s would be 26 ops, minimum
            // So this saves about 21 ops:
            // probably more since it doesn't call a method and Arrays doesn't call checks
            for(i = 0; i < WORDLEN; i++){
                finds[resets[i]] = 0;
            }

            // First, check 2-states
            for(i = 0; i < WORDLEN; i++){
                if(guess[i] == word[i]){
                    // 2 state
                    state[i] = true;
                }
                else{
                    // Not a 2-state
                    // Store letter occurences for 1-state or 0-state
                    int letter = word[i]-'a';
                    finds[letter]++;
                }
            }

            int stateBase3 = 0;
            for(i = 0; i < WORDLEN; i++){
                stateBase3 *= 3; // doing this anyway no matter what, so let's do it ahead of time.
                if(state[i]){
                    state[i] = false; // reset it for the next run
                    stateBase3 += 2; // 2-state, so add 2
                    continue;
                }
                int letter = guess[i] - 'a';

                if(finds[letter] != 0){
                    // Letter found!
                    finds[letter]--;
                    stateBase3++; // 1-state, add 1
                }
                // 0 state is adding 0, aka doing nothing
            }

            // Finally, update states
            states[stateBase3]++;
        }

        long quality = 0;
        // Why up to 3^WORDLEN-1?
        // Because at 22222 (base 3) it's just the exact same word
        // And in cases where only one word remains we want that one word to have the highest quality
        // And in cases where exactly two words remain we want it to prioritize those two words
        // over picking any other word that would solve the difference between the two
        for(i = 0; i < THREE_POW_WORDLEN-1; i++){
            // the quality of just this state
            long stateQual = states[i];
            stateQual *= states[i];

            // I assume the compiler makes this a much faster function, anyway.
            quality += stateQual;
        }
        return quality;
    }

    // Finds the best guess.
    // This is the ONLY reason why _guesses is needed.
    public char[] FindBestGuess(){
        char[] bestWord = _guesses[0];
        long bestQual = QualifyGuess(_guesses[0]);

        for(char[] word : _answers){
            long quality = QualifyGuess(word);
            if(quality < bestQual){
                bestWord = word;
                bestQual = quality;
            }
        }

        return bestWord;
    }

    public List<char[]> getAnswers() {
        return _answers;
    }

    // Note:
    // When the string is too long for the byte-array, it truncates the FIRST digits
    // When the string is too short, the string placed at the END of the byte-array
    // and the FIRST few digits are 0s.
    static byte[] ToStateArray(String in){
        char[] charArray = in.toCharArray();
        byte[] out = new byte[WORDLEN];
        for(int i = WORDLEN-1; i >= 0; i--){
            char c = charArray[i];
            if(c < '0' || c > '2'){
                throw new IllegalArgumentException("States can only be 0, 1, or 2. Nothing else.");
            }
            out[i] = (byte) (c - '0');
        }
        return out;
    }
}
