package Labs;
import java.util.Scanner;
public class Lab1 {
    public static void Solution() {
        Scanner sc = new Scanner(System.in);
        int one = sc.nextInt(), two = sc.nextInt(), three = sc.nextInt();

        int min = Math.min(Math.min(one, two), Math.min(two, three) );
        int max = Math.max(Math.max(one, two), Math.max(two, three) );
        int middle = one + two + three - min - max;

        if(middle - min == max - middle)
            System.out.println("NA");
        else if(middle - min > max - middle)
            System.out.println(min);
        else System.out.println(max);
    }
}
