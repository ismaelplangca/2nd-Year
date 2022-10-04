package Lab6Cryptography;

import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/* g = random generator number (prime number)
 * p = modulus
 * g^x mod p = result of equation
 */
/* Public key; p, g, g^x mod p
 * 9859 3399 4300
 */
/* Receiver sent information; g^y mod p, m.g^xy mod p
 * 6727 5767
 */
/* DES-Encrypted communication payload;
 * -103, -41, -88, -49, -71, 125, -83, 25, -41, -10, -113, -115, -43, 96, -66, 101, -8, -65, 60, -81, 73, 107, 83, -38
 * Bomb Kiev at dawn
 */
public class Lab6 {
    /* Identify the shared key to reverse the DES encryption
     * Shared key; encrypting key, decrypting key
     *
     * // Required;
     * // g^x and y
     * // g^y and x
     */
    public static void solution() {
        Scanner sc = new Scanner(System.in);
        // p, g, b = g^x mod p
        long[] firstLine  = split(3, sc.nextLine() );
        long p = firstLine[0];
        long g = firstLine[1];
        long b = firstLine[2];

        // c1 = g^y mod p, c2 = m.g^xy mod p
        long[] secondLine = split(2, sc.nextLine() );
        long c1 = secondLine[0];
        long c2 = secondLine[1];

        String thirdLine = sc.nextLine();

        long x = 0L;
        // Brute force
        // Lab constraints: 1 <= p <= 9,223,372,036,854,775,807
        for(long pow = 0L; pow < p; pow++) {
            if(modPow(g, pow, p) == b) {
                System.out.println(x = pow);
                break;
            }
        }
        System.out.println("Baby step-Giant step: " + giantbaby(p,g,b) );

        // calculate: c2/(c1^x)
        // a = c1^(p-1-x)
        // a.c2 mod p = m
        // System.out.println(modPow(90429,149887, 150001) );
        long sharedKey = modMult(modPow(c1, p-1-x, p), c2, p);
        System.out.println(sharedKey);
        String output = decrypt(sharedKey, thirdLine);
        System.out.println(output);
    }

    /* Input:
     * 9859 3399 4300
     * 6727 5767
     * -103, -41, -88, -49, -71, 125, -83, 25, -41, -10, -113, -115, -43, 96, -66, 101, -8, -65, 60, -81, 73, 107, 83, -38
     */
    public static void tutorial() {
        Scanner sc = new Scanner(System.in);
        String[] firstLine = sc.nextLine().split("\\s+");
        long p = Long.parseLong(firstLine[0]);
        long g = Long.parseLong(firstLine[1]);
        long b = Long.parseLong(firstLine[2]); // b = g^x mod p

        String[] secondLine = sc.nextLine().split("\\s+");
        long c1 = Long.parseLong(secondLine[0]); // g^y mod p
        long c2 = Long.parseLong(secondLine[1]); // m.g^xy mod p

        String byteList = sc.nextLine();

        long x = 0L;
        for(int i = 0; i < p; i++) {
            if(modPow(g,i,p) == b) {
                x = i;
                break;
            }
        }

        // m = c2.c1^(p-1-x)
        long m = modMult(c2,modPow(c1,p-1-x,p),p);
        String output = decrypt(m,byteList);
        System.out.println(output);
    }

    public static long giantbaby(long p, long g, long B) {
        ///*
        int ceiling = (int)Math.sqrt(p-1);
        // populate table with g^j mod p up until ceiling
        Hashtable<Integer, Long> table =
                IntStream.iterate(0, j -> j <= ceiling, j -> j + 1)
                        .boxed()
                        .collect(Collectors.toMap(
                                j -> j,
                                j -> modPow(g,j,p),
                                (a, b) -> b,
                                () -> new Hashtable<>(ceiling+1)
                            )
                        );
        System.out.println(table);
        // Check for collisions
        long c = modPow(g,ceiling*(p-2),p);
        for(int i = 0; i < ceiling; i++) {
            long val = modMult(B,modPow(c,i,p),p);
            for(Map.Entry<Integer,Long> entry : table.entrySet() )
                if(entry.getValue() == val)
                    return entry.getKey();
        }
        //*/
        return 0L;
    }

    public static long[] split(int n, String input) {
        long[] arr = new long[n];
        for(int i = 0, pos = 0, index = 0; i < input.length(); i++) {
            if(input.charAt(i) == ' ') {
                arr[index++] = Long.parseLong(input.substring(pos, i) );
                pos = ++i;
                if(--n == 1) {
                    arr[index] = Long.parseLong(input.substring(pos) );
                    break;
                }
            }
        }
        return arr;
    }

    public static String decrypt(long sharedKey, String byteList) {
        // Send this method, the shared key and byteList read from the third input line, and it will decrypt using DES and return the decrypted String
        try {
            byte[] keyBytes = new byte[8];
            byte[] ivBytes = new byte[8];
            for (int i = 7; i >= 0; i--) {
                keyBytes[i] = (byte)(sharedKey & 0xFF);
                ivBytes[i] = (byte)(sharedKey & 0xFF);
                sharedKey >>= 8;
            }

            // Wrap key data in Key/IV specs to pass to cipher
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            // Create the cipher with the algorithm you choose
            // See javadoc for Cipher class for more info, e.g.
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

            String[] process = byteList.split(", ");
            int enc_len = process.length;
            byte[] encrypted = new byte[cipher.getOutputSize(enc_len)];
            for(int i = 0; i < process.length; i++)
                encrypted[i] = (byte)(Integer.parseInt(process[i]) );

            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] decrypted = new byte[cipher.getOutputSize(enc_len)];
            int dec_len = cipher.update(encrypted, 0, enc_len, decrypted, 0);
            cipher.doFinal(decrypted, dec_len);

            return (new String(decrypted, StandardCharsets.UTF_8).trim() );
        } catch(Exception e) { return "Error: " + e; }
    }

    /**
     * Raises a number to a power with the given modulus
     * @param number
     * @param power
     * @param modulus
     * @return number^power % modulus
     */
    public static long modPow(long number, long power, long modulus) {
        // When raising a number to a power, the number quickly becomes too large to handle
        // You need to multiply numbers in such a way that the result is consistently moduloed to keep it in the range
        // However you want the algorithm to work quickly - having a multiplication loop would result in an O(n) algorithm!
        // The trick is to use recursion - keep breaking the problem down into smaller pieces and use the modMult method to join them back together
        if(power == 0)
            return 1;
        if(power % 2 == 0) {
            long halfPower = modPow(number, power/2, modulus);
            return modMult(halfPower, halfPower, modulus);
        }
        long halfPower = modPow(number, power/2, modulus);
        long firstBit = modMult(halfPower, halfPower, modulus);
        return modMult(firstBit, number, modulus);
    }

    /**
     * Multiplies the first number by the second number with the given modulus
     * @param first
     * @param second
     * @param modulus
     * @return (first*second) % modulus
     */
    public static long modMult(long first, long second, long modulus) {
        // A long can have a maximum of 19 digits. Therefore, if you're multiplying two ten digits numbers the usual way, things will go wrong
        // You need to multiply numbers in such a way that the result is consistently moduloed to keep it in the range
        // However you want the algorithm to work quickly - having an addition loop would result in an O(n) algorithm!
        // The trick is to use recursion - keep breaking down the multiplication into smaller pieces and mod each of the pieces individually
        if(second == 0)
            return 0;
        if(second % 2 == 0) {
            long half = modMult(first, second/2, modulus);
            return (half + half) % modulus;
        }
        long half = modMult(first, second/2, modulus);
        return (half+half+first)%modulus;
    }
}
