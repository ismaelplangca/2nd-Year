package Labs;
import java.util.Scanner;
public class Lab3 {
    public static void Solution() {
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        int nextPrime = input+1, prevPrime = input;
        while(!isPrime(nextPrime) ) nextPrime++;
        while(!isPrime(prevPrime) ) prevPrime++;
        System.out.println(nextPrime-prevPrime);
    }
    public static boolean isPrime(int n) {
        for(int i = 2; i <= Math.sqrt(n); i++) {
            if(n%i == 0) return false;
        }
        return true;
    }
}
