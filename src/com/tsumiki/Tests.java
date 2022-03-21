package com.tsumiki;

public class Tests {
    public static void TestOne(){
        WordleGuesser wordle = new WordleGuesser(Main.GetWords());

        assert wordle.getAnswers().size() == 12972;

        wordle.ApplyGuess("track".toCharArray(), new byte[]{0,0,1,0,0});
        assert wordle.getAnswers().size() == 1647;

        wordle.ApplyGuess("among".toCharArray(), new byte[]{2,0,1,0,0});
        assert wordle.getAnswers().size() == 16;

        wordle.ApplyGuess("lapse".toCharArray(), new byte[]{1,1,0,0,0});
        assert wordle.getAnswers().size() == 6;
    }

    public static void TestTwo(){
        MultiWordleGuesser quordle = new MultiWordleGuesser(4, Main.GetWords());

        assert EqualCharArrays(quordle.FindBestGuess(), "lares");
        quordle.ApplyGuess("lares".toCharArray(), new byte[][]{
                new byte[] {1,0,0,0,0},
                new byte[] {0,0,1,1,0},
                new byte[] {0,1,0,0,2},
                new byte[] {0,0,0,1,1},
        });

        assert EqualCharArrays(quordle.FindBestGuess(), "colly");
        quordle.ApplyGuess("colly".toCharArray(), new byte[][]{
                new byte[] {0,0,1,0,0},
                new byte[] {2,1,0,0,0},
                new byte[] {0,0,0,0,0},
                new byte[] {0,1,0,0,0},
        });

        assert EqualCharArrays(quordle.FindBestGuess(), "pling");
        quordle.ApplyGuess("pling".toCharArray(), new byte[][]{
                new byte[] {0,2,0,0,0},
                new byte[] {0,0,0,2,0},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,0,2,0},
        });

        assert EqualCharArrays(quordle.FindBestGuess(), "crone");
        quordle.ApplyGuess("crone".toCharArray(), new byte[][]{
                new byte[] {0,0,0,0,0},
                new byte[] {2,2,2,2,2},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,2,2,2},
        });

        assert EqualCharArrays(quordle.FindBestGuess(), "bluff");
        quordle.ApplyGuess("bluff".toCharArray(), new byte[][]{
                new byte[] {0,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,0,0,0},
        });

        assert EqualCharArrays(quordle.FindBestGuess(), "fluff");
        quordle.ApplyGuess("fluff".toCharArray(), new byte[][]{
                new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {0,0,0,0,0},
                new byte[] {0,0,0,0,0},
        });

        assert EqualCharArrays(quordle.FindBestGuess(), "shone");
        quordle.ApplyGuess("shone".toCharArray(), new byte[][]{
                // new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {1,0,0,0,0},
                new byte[] {2,0,2,2,2},
        });

        assert EqualCharArrays(quordle.FindBestGuess(), "stone");
        quordle.ApplyGuess("stone".toCharArray(), new byte[][]{
                // new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {1,0,0,0,0},
                new byte[] {2,2,2,2,2},
        });

        assert EqualCharArrays(quordle.FindBestGuess(), "amass");
        quordle.ApplyGuess("amass".toCharArray(), new byte[][]{
                // new byte[] {2,2,2,2,2},
                // new byte[] {2,2,2,2,2},
                new byte[] {2,2,2,2,2},
                // new byte[] {2,0,2,2,2},
        });
    }

    public static boolean EqualCharArrays(char[] a, String b){
        return EqualCharArrays(a, b.toCharArray());
    }

    public static boolean EqualCharArrays(String a, char[] b){
        return EqualCharArrays(a.toCharArray(), b);
    }

    public static boolean EqualCharArrays(char[] a, char[] b){
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
