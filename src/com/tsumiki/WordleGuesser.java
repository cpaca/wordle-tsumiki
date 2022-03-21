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
    public void ApplyGuess(final char[] inGuess, final byte[] inState){
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

            // was named test because I was testing if this would pass the tests
            // and yeah, it does
            // dunno how tho
            byte[] test = new byte[WORDLEN];
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
                        if(test[j] != 0){
                            continue;
                        }

                        // Already processed by the two-state
                        // Ignore
                        if(state[j]/2 != 0){
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
                            test[j] = 1;

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

    // Returns the quality of a guess
    // Note, this is like golf, so you want this to return as SMALL of a value as possible.
    // If the input is invalid, this returns the worst possible quality.
    public long QualifyGuess(final char[] inGuess){
        if(inGuess.length != WORDLEN){
            return Long.MAX_VALUE; // Invalid input.
        }

        char[] guess = new char[WORDLEN];
        System.arraycopy(inGuess, 0, guess, 0, WORDLEN);

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

        // State, base-3 representation
        // It's probably easier on the GC to do this
        // Since Java initializes it all to 0s anyway, which is the same thing Arrays.fill() does
        // so this is faster since it skips the initialization/adding to GC watcher/destruction set
        byte[] state = new byte[WORDLEN];

        // Whether it's been proced by the 1-state on J index
        // Note this is also set to 1 on 2-state procs
        // Honestly I remember writing it and thinking "This is probably how to solve it, but I dunno why"
        byte[] JIndexProc = new byte[WORDLEN];

        for(char[] word : _answers){
            // For the first loop, state is initialized to a bunch of 0s
            // There's a thing later on that resets state[i].
            // It's right when it calculates the base-3 representation.

            // I doubt it matters much
            // but maybe this saves some time.
            int i;

            // First, check 2-states
            for(i = 0; i < WORDLEN; i++){
                if(guess[i] == word[i]){
                    state[i] = 2;
                    JIndexProc[i] = 1;
                }
            }

            // Then, check 1-states
            for(i = 0; i < WORDLEN; i++){
                if(state[i] == 2){
                    continue;
                }
                for(int j = 0; j < WORDLEN; j++){
                    if(JIndexProc[j] != 0){
                        // already handled by 1-proc or 2-proc
                        continue;
                    }
                    if (guess[i] == word[j]) {
                        state[i] = 1;
                        JIndexProc[j] = 1;
                        break;
                    }
                }
            }

            // Then, find the base-3 representation
            int stateBase3 = 0;
            for(i = 0; i < WORDLEN; i++){
                stateBase3 = stateBase3*3 + state[i];
                // While I'm here, why not reset the state to 0s?
                state[i] = 0;
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
        for(int i = 0; i < THREE_POW_WORDLEN-1; i++){
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
