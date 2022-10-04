package Labs;
import java.util.Scanner;
public class Lab2 {
    public static void Solution() {
        Scanner sc = new Scanner(System.in);

        int one = Math.abs(sc.nextInt() ), two = Math.abs(sc.nextInt() ), three = Math.abs(sc.nextInt() );
        int min = Math.min(Math.min(one, two), Math.min(two, three) );

        if( one + two + three == 0)
            System.out.println("0");
        else if(one == 0 || two == 0 || three == 0)
            System.out.println("NA");
        else {
            int multiple = min;
            while(!(multiple%one == 0 && multiple%two == 0 && multiple%three == 0) )
                multiple = multiple+min;
            System.out.println(multiple);
        }
    }
}
