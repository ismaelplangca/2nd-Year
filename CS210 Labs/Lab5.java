package Labs;
import java.util.Scanner;
public class Lab5 {
    public static void Solution() {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine() );
        String[] arr = new String[n];
        for(int i = 0; i < n; i++)
            arr[i] = sc.nextLine();
        sort(arr);
        for(String word : arr)
            System.out.println(word);
    }
    public static void sort(String[] arr) {
        int n = arr.length;
        for(int i = 0; i < n-1; i++) {
            for(int j = 0; j < n-i-1; j++) {
                if(check(arr[j], arr[j+1]) ) {
                    String temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
    public static boolean check(String first, String second) {
        int[] vals = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
        int fVal = 0, sVal = 0;
        for(int i = 0; i < first.length(); i++)
            fVal += vals[(int)(first.charAt(i))-'a']; // -97
        for(int i = 0; i < second.length(); i++)
            sVal += vals[(int)(second.charAt(i))-'a'];
        return fVal > sVal;
    }
}
