package Labs;
import Stacks.*;
import java.util.Scanner;
public class Lab6 {
    public static void Solution() {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine() );
        Stack myStack = new Stack(n);
        for(int i = 0; i < n; i++) {
            String input = sc.nextLine();
            if(input.equals("POP") ) {
                if(myStack.size() > 0)
                    myStack.pop();
            } else myStack.push(Integer.parseInt(input.split(" ")[1]) );
        }
        if(myStack.size() == 0) System.out.println("empty");
        else System.out.println(myStack.pop() );
    }
}
