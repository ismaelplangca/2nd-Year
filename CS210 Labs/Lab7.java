package Labs;
import Queues.*;
import java.util.Scanner;
public class Lab7 {
    public static void Solution() {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine() );
        Queue q = new Queue(n);
        while(sc.hasNextLine() ) {
            String[] input = sc.nextLine().split("\\s+");
            if(input[0].equalsIgnoreCase("REMOVE") ) {
                if(q.size() > 0) q.remove();
            } else q.insert(input[1]);
        }
        if(q.size() == 0) System.out.println("empty");
        else {
            int size = q.size();
            for(int i = (size+size%2)/2-1; i > 0; i--)
                q.remove();
            System.out.println(q.remove() );
        }
    }
}
