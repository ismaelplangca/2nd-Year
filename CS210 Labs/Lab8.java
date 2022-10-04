package Labs;
import Lists.*;
import java.util.Scanner;
public class Lab8 {
    public static void Solution() {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine() );
        Link[] arr = new Link[n];
        for(int i = 0; i < n; i++) {
            arr[i] = new Link(Integer.parseInt(sc.nextLine() ) );
        }
        while(sc.hasNext() ) {
            int select = sc.nextInt(), prev = sc.nextInt(), next = sc.nextInt();
            if(prev != -1) arr[select].prev = arr[prev];
            if(next != -1) arr[select].next = arr[next];
        }
        DoublyLinkedList listKo = new DoublyLinkedList();
        if(n > 0) {
            listKo.first = arr[0];
            listKo.last = arr[n-1];
        }
        System.out.println(search(listKo) );
    }
    public static int search(DoublyLinkedList listKo) {
        if(listKo.isEmpty() ) return -1;

        Link check = listKo.first;
        while(check.next != null) {
            if(check.next.prev != check) return 0;
            check = check.next;
        }

        Link curr = listKo.first;
        int sm = Integer.MAX_VALUE, sm2 = Integer.MAX_VALUE;
        while(curr != null) {
            int currInt = curr.data;
            if(currInt < sm) {
                sm2 = sm;
                sm = currInt;
            } else if(currInt < sm2 && currInt != sm)
                sm2 = currInt;
            curr = curr.next;
        }
        return (sm2 == Integer.MAX_VALUE) ? -1 : sm2;
    }
}
