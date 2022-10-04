package Lab11Hangman;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static MyUtil.Printer.*;

public class Lab11 {
    public static void solution() {
        String[] dictionary = null;
        try(var lines = Files.lines(Path.of("allowable.txt") ) ) {
            dictionary = lines.map(String::toLowerCase).toArray(String[]::new);
        } catch (Exception ignored) {}

        var target = dictionary[new Random().nextInt(dictionary.length)];
        println(target);
        var blackout = "_".repeat(target.length() );

        HangmanAI hai = new HangmanAI(dictionary, blackout);
        int lives = 0, score = 0;
        var running = true;

        while(running) {
            var guess = hai.guessLetter();
            var original = hai.hiddenWord;
            println(original);
            char[] arrayForm = original.toCharArray();

            for(int i = 0; i < target.length(); i++)
                if(target.charAt(i) == guess)
                    arrayForm[i] = guess;

            String newForm = new String(arrayForm);

            hai.hiddenWord = newForm;
            println(newForm);
            if(newForm.equals(original) )
                lives--;
            if(lives == 0)
                running = false;
            if(hai.hiddenWord.equals(target) ) {
                running = true;
                score++;
            }
        }
    }
}

class HangmanAI {
    public ArrayList<String> dictionary;
    public String hiddenWord;
    private final String letterFreq;
    private boolean firstAttempt;
    private boolean[] found;
    private int index;

    public HangmanAI(String[] wordlist, String target) {
        dictionary = new ArrayList<>(Arrays.asList(wordlist) );
        hiddenWord = target;
        letterFreq = "etaoinshrdlcumwfgypbvkjxqz";
        firstAttempt = true;
        found = new boolean[hiddenWord.length()];
        index = 0;
        dictionary.removeIf(word -> word.length() != hiddenWord.length() );
    }

    public char guessLetter() {
        // Guess the letter e
        if(firstAttempt) {
            firstAttempt = false;
            return letterFreq.charAt(index++);
        }

        // Remove words from the dictionary that don't contain a letter in the hidden word
        for(int i = 0; i < hiddenWord.length(); i++) {
            if(found[i]) continue;
            char c = hiddenWord.charAt(i);
            if(c != '_') {
                found[i] = true;
                final int pos = i;
                dictionary.removeIf(word -> word.charAt(pos) != c);
            }
        }

        for(; index < letterFreq.length(); index++)
            for(String word : dictionary) {
                char c = letterFreq.charAt(index);
                if(word.contains(Character.toString(c) ) )
                    return letterFreq.charAt(index++);
            }

        return '0';
    }
}
