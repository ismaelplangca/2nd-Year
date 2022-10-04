package Lab8BitManipulation;
import java.util.Scanner;
public class Lab8 {
    public static void solution() {
        Scanner sc = new Scanner(System.in);
        System.out.println(
                changeBase(
                        Integer.parseInt(sc.nextLine() ),
                        Integer.parseInt(sc.nextLine() ),
                        sc.nextLine()
                )
        );
    }

    public static String changeBase(int b1, int b2, String n) {
        String nums = "0123456789ABCDEF";
        return decimalToBase(b2, baseToDecimal(b1, n, nums), nums);
    }

    public static String decimalToBase(int b, int n, String nums) {
        StringBuilder str = new StringBuilder();
        while(n != 0) {
            str.append(nums.charAt(n % b) );
            n /= b;
        }
        return str.reverse().toString();
    }

    public static int baseToDecimal(int b, String n, String nums) {
        int mult = 1, res = 0;
        for(int i = n.length()-1; i >= 0; i--) {
            int digit = nums.indexOf(Character.toString(n.charAt(i) ) );
            res += digit * mult;
            mult *= b;
        }
        return res;
    }
}