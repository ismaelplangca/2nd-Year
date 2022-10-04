package Labs;
import java.util.Scanner;
public class Lab4 {
    public static void Solution() {
        Scanner sc = new Scanner(System.in);
        int classSize = sc.nextInt(), matches = sc.nextInt();
        double trials = 100000, successes = 0;
        for(int i = 0; i < trials; i++) {
            int[] days = new int[365];
            for(int j = 0; j < classSize; j++) {
                int birthday = (int)(Math.random()*365);
                days[birthday]++;
                if(days[birthday] >= matches) {
                    successes++;
                    break;
                }
            }
        }
        System.out.println(Math.round(successes*100/trials) );
    }
}
