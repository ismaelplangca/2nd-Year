/**
 * CS210 Project Jan 2022
 * 20466014
 * Ismael Christian Plangca
 * Computer Science & Software Engineering
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CS210Project {
    public static void main(String[] args) throws IOException {
        ///*
        System.out.println(compareHash(
                sha256("Fun, frothy, romantic."),
                sha256("The quality has definitely gone down.") )
        ); // 22
        System.out.println(compareHash(
                sha256("When will we see Arrakis as a paradise?"),
                sha256("Guybon, Talmanes said, turning back.") )
        ); // 22
        //System.exit(0);
        //*/
        long start = System.currentTimeMillis();
        ArrayList<Sentence> sentences = Arrays.stream(load(new File("books.txt") ) )
                .parallel()
                .map(Sentence::new)
                .collect(Collectors.toCollection(ArrayList::new) );
        // ArrayList<Sentence> sentences = loadBooks();
        // sentences.addAll(sentenceGenerator() );
        System.out.println("Loading complete: " + (System.currentTimeMillis()-start) );
        int count, hiCount = -1;
        StringBuilder hiStrings = new StringBuilder();
        for(int i = 0, len = sentences.size(); i < len; i++) {
            for(int j = i+1; j < len; j++) {
                count = compareHash(sentences.get(i).getHash(), sentences.get(j).getHash() );
                // If count is 64 then chances are the strings are the same
                if(count == 64 || count < hiCount) continue;
                if(count == hiCount) {
                    // If true, append sentences and count
                    // so that more than one pair of sentences will be printed
                    hiStrings
                            .append("\n").append(sentences.get(i).toString() )
                            .append("\n").append(sentences.get(j).toString() )
                            .append("\nCount: ").append(count);
                } else {
                    // count > hiCount
                    // replace hiCount and hiStrings
                    hiCount = count;
                    hiStrings = new StringBuilder(
                            sentences.get(i).toString() ).append("\n")
                            .append(sentences.get(j).toString() )
                            .append("\nCount: ").append(hiCount);
                }
            }
        }
        System.out.println(hiStrings);

        /*
        sentenceCreator senCr1 = new sentenceCreator(); // i
        sentenceCreator senCr2 = new sentenceCreator(); // j
        int score, hiCount = -1;
        StringBuilder sb = new StringBuilder();
        while(senCr1.getCurrent() != null) {
            String hash1 = sha256(senCr1.getCurrent() );
            // j = i + 1;
            senCr2.setIndexes(senCr1.getIndexes() );
            senCr2.incrementIndexes();
            while(senCr2.getCurrent() != null) {
                score = compareHash(hash1, sha256(senCr2.getCurrent() ) );
                if(score == hiCount) {
                    sb.append("\n").append(senCr1.getCurrent() )
                            .append("\n").append(senCr2.getCurrent() )
                            .append("\n").append(score);
                } else if(score > hiCount) {
                    hiCount = score;
                    sb = new StringBuilder(senCr1.getCurrent() )
                            .append("\n").append(senCr2.getCurrent() )
                            .append("\n").append(score);
                }
                senCr2.incrementIndexes();
            }
            senCr1.incrementIndexes();
        }
         */

        long end = System.currentTimeMillis();
        System.out.println("ms: " + (end - start) );
    }
    /*
    public static void writeToFile(ArrayList<Sentence> sentences) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("books.txt") );
            for(Sentence line : sentences) {
                writer.write(line.getSentence() );
                writer.newLine();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(writer != null)
                    writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
     */
    public static String sha256(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            byte[] salt = "CS210+".getBytes(StandardCharsets.UTF_8);
            mDigest.update(salt);
            byte[] data = mDigest.digest(input.getBytes(StandardCharsets.UTF_8) );
            StringBuilder sb = new StringBuilder();
            for(byte datum : data)
                sb.append(Integer.toString((datum&0xff)+0x100,16).substring(1) );
            return sb.toString();
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    public static int compareHash(String first, String second) {
        int count = 0;
        for(int i = -1; ++i < 64;)
            if(first.charAt(i) == second.charAt(i) )
                count++;
        return count;
    }
    public static boolean wordLim(String input) {
        int spaces = 0, inpLen = input.length();
        for(int i = -1; ++i < inpLen;)
            if(input.charAt(i) == ' ')
                spaces++;
        // Sentences with only 2 words rarely make sense
        return spaces > 1 && spaces < 10;
    }

    /**
     * Parses a String array for valid project sentences.
     * @return An ArrayList of Sentence Objects
     */
    public static ArrayList<Sentence> getSentences(String[] input) {
        // regex for chars that may invalidate sentences grammatically.
        Pattern invalidChars = Pattern.compile("[\"()“”‘*]");
        ArrayList<Sentence> arr = new ArrayList<>(5000); // Estimated lower limit for valid book sentences
        int pos = 0; // position of the start of the line
        for(String line : input) {
            String clean = invalidChars.matcher(line).replaceAll("");
            // Iterate through current clean line
            for(int j = 0, lineLen = clean.length(); j < lineLen; j++) {
                // Considers the substring from pos to punctuation mark.
                if(clean.charAt(j) == '.' || clean.charAt(j) == '?' || clean.charAt(j) == '!') {
                    if(wordLim(clean.substring(pos, j) ) ) {
                        // If true, add sentence to arr
                        arr.add(new Sentence(clean.substring(pos, j+1) ) );
                    }
                    // Move pos and j to the next sentence
                    while(clean.charAt(j) != ' ' && j < lineLen - 1) {
                        j++;
                        pos = j + 1;
                    }
                }
            }
            pos = 0; // Reset pos for new line
        }
        return arr;
    }

    public static String[] load(File file) {
            StringBuilder contents = new StringBuilder();
            BufferedReader input = null;
            try {
                input = new BufferedReader(new FileReader(file.getAbsoluteFile() ) );
                String line;
                while((line = input.readLine() ) != null)
                    if(!line.isBlank() )
                        contents.append(line)
                                .append(System.getProperty("line.separator") );
            } catch(IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if(input != null)
                        input.close();
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
            String[] arr = contents.toString().split("\n");
            for(int i = 0, len = arr.length; i < len; i++)
                arr[i] = arr[i].trim();
            return arr;
    }
    /**
     * Returns an ArrayList of Sentence objects extracted from "Books" directory.
     * All files in the directory are .txt files.
     */
    public static ArrayList<Sentence> loadBooks() throws IOException {
        /*
         Walk through "Books" directory
         Stream paths into files
         Filter directories
         Stream the String arrays loaded from files
            as a single string stream by amalgamating/flattening the arrays as one
         Stream every String as a Sentence
         Collect resulting stream in an ArrayList
        */
        return Files.walk(Path.of("Books") )
                .map(Path::toFile)
                .filter(file -> !file.isDirectory() )
                .flatMap(txt -> getSentences(load(txt) ).stream() )
                .collect(Collectors.toCollection(ArrayList::new) );
    }
    /**
     * Returns an ArrayList of Sentence objects created from a rudimentary sentence generator.
     */
    public static ArrayList<Sentence> sentenceGenerator() {
        String[] sen1 = {"My","Your","Her","His","Our","Their"};
        String[] sen4 = {"was","will be"};
        String[] sen6 = {".","!","?"};
        String[] sen = new String[150];
        // adjectives -> nouns -> verbs, 50 each

        // load method
        BufferedReader input = null;
        int index = 0;
        try {
            input = new BufferedReader(new FileReader("words.txt") );
            String line;
            while((line = input.readLine() ) != null)
                sen[index++] = line;
        } catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(input != null)
                    input.close();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }

        // Sentence generator
        ArrayList<Sentence> sentences = new ArrayList<>(4500000);
        StringBuilder str;
        // Adds all combinations of strings
        for(String s1 : sen1)
            for(int i = 0; i < 50; i++)
                for(int j = 50; j < 100; j++)
                    for(String s4 : sen4)
                        for(int k = 100; k < 150; k++)
                            for(String s6 : sen6) {
                                str = new StringBuilder(s1).append(" ")
                                        .append(sen[i]).append(" ")
                                        .append(sen[j]).append(" ")
                                        .append(s4).append(" ")
                                        .append(sen[k]).append(s6);
                                sentences.add(new Sentence(str.toString() ) );
                            }
        return sentences;
    }
}
/**
 * A data carrier class that carries a String and its SHA-256 hash.
 */
class Sentence {
    private final String sentence, hash;
    public Sentence(String sentence) {
        this.sentence = sentence;
        hash = CS210Project.sha256(sentence);
    }
    public String getSentence() {
        return sentence;
    }
    public String getHash() {
        return hash;
    }
    @Override
    public String toString() {
        return "Sentence: " + sentence + "\nHash: " + hash;
    }
}
/**
 * Provides sequential access to String combinations created by a sentence generator.
 * Represents the maximum generator total of 1,083,948,444 String combinations.
 */
class sentenceCreator {
    private final String[] sen1, sen2, sen3, sen4, sen5, sen6;
    private int i1, i2, i3, i4, i5, i6;
    private StringBuilder curr;
    /**
     * Constructs a sentence generator with starting indexes of zeroes.
     */
    public sentenceCreator() {
        sen1 = new String[]{"My","Your","Her","His","Our","Their"}; // 6
        sen2 = CS210Project.load(new File("Generator/adj.txt") ); // 907
        sen3 = CS210Project.load(new File("Generator/nouns.txt") ); // 89
        sen4 = new String[]{"was","will be"}; // 2
        sen5 = CS210Project.load(new File("Generator/verbs.txt") ); // 373
        sen6 = new String[]{".","!","?"}; // 3
        i1 = i2 = i3 = i4 = i5 = i6 = 0;
        updateCurrent();
    }

    public String getCurrent() {
        return curr.toString();
    }
    /**
     * Updates the current sentence combination to
     * reflect changes made to the indexes.
     */
    private void updateCurrent() {
        curr = new StringBuilder(sen1[i1]).append(" ")
                .append(sen2[i2]).append(" ")
                .append(sen3[i3]).append(" ")
                .append(sen4[i4]).append(" ")
                .append(sen5[i5]).append(sen6[i6]);
    }
    /**
     * Increments sentence generator array indexes to create the next string combination.
     * Returns null and resets the indexes when all possible combinations have been created.
     */
    public void incrementIndexes() {
        if(i6 + 1 < 3) {
            i6++;
            updateCurrent();
            return;
        }
        i6 = 0;
        if(i5 + 1 < 373) {
            i5++;
            updateCurrent();
            return;
        }
        i5 = 0;
        if(i4 + 1 < 2) {
            i4++;
            updateCurrent();
            return;
        }
        i4 = 0;
        if(i3 + 1 < 89) {
            i3++;
            updateCurrent();
            return;
        }
        i3 = 0;
        if(i2 + 1 < 907) {
            i2++;
            updateCurrent();
            return;
        }
        i2 = 0;
        if(i1 + 1 < 6) {
            i1++;
            updateCurrent();
            return;
        }
        // -1 is assigned to i6 so that
        // the next incrementIndexes call will increment i6 to 0;
        i6 = -1;
        i1 = 0; // Overflows and resets the counters
        curr = null;
    }
    /**
     * Returns sentence generator indexes as an int array.
     * Returns the next iteration of indexes if current String is null
     */
    public int[] getIndexes() {
        return i6 == -1
                ? new int[]{i1,i2,i3,i4,i5,0}
                : new int[]{i1,i2,i3,i4,i5,i6};
    }
    /**
     * Replaces sentence generator array indexes with the corresponding elements of the input array.
     * Updates current string combination with the new indexes.
     */
    public void setIndexes(int[] indexes) {
        // if(indexes.length != 6) return;
        i1 = indexes[0];
        i2 = indexes[1];
        i3 = indexes[2];
        i4 = indexes[3];
        i5 = indexes[4];
        i6 = indexes[5];
        updateCurrent();
    }
}