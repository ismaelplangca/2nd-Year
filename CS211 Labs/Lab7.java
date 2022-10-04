package Lab7HashTables;

public class Lab7 {
    public static void solution() {
        String[] array = {
                "Is","one","more","love","out","to","break","your","heart",
                "Set","it","up","just","to","watch","it","fall","apart"
        };
        HashTable hashTable = new HashTable(fill(array.length, array) );
        System.out.println("pos=" + find(array.length, hashTable,"just") );
        System.out.println("collisions=" + hashTable.getTotal() );
    }

    public static int find(int size, HashTable mytable, String word) {
        // Fill this in as to minimise collisions
        // This method should return the slot in the hashtable where the word is
        int hash = hashcode(word) % size;
        int quadProbe = 1;
        while(!mytable.check(hash, word) ) {
            hash += quadProbe * quadProbe;
            quadProbe++;
            hash %= size;
        }
        return hash;
    }

    public static String[] fill(int size, String[] array) {
        // This should add all the words into the hashtable using some system
        // then it should return the hashtable array
        String[] hashtable = new String[size];
        for(int i = 0; i < size; i++)
            hashtable[i] = "";

        for(String s : array) {
            int hash = hashcode(s) % size;
            int quadProbe = 1;
            while(!hashtable[hash].equals("") ) {
                hash += quadProbe * quadProbe;
                quadProbe++;
                hash %= size;
            }
            hashtable[hash] = s;
        }
        return hashtable;
    }

    // inp[0]*31^(n-1) + inp[1]*31^(n-2) + ... + inp[n-1]
    public static int hashcode(String input) {
        int h = 0;
        for(int i = 0; i < input.length(); i++)
            h = 31 * h + (input.charAt(i) & 0xff);
        // h += (int)(input.charAt(i)*Math.pow(<prime>,i) );
        return Math.abs(h);
    }

    public static int hashcode(byte[] value) {
        int h = 0;
        for (byte v : value) {
            h = 31 * h + (v & 0xff);
        }
        return h;
    }

}
class HashTable {
    private final String[] hashTable;
    private int total = 0;

    public HashTable(String[] input) {
        hashTable = input;
    }

    public boolean check(int slot, String check) {
        if(hashTable[slot].equals(check) )
            return true;
        total++;
        return false;
    }

    public int getTotal() {
        return total;
    }
}
