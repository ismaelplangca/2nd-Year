package Labs;
import java.util.Scanner;
public class Lab10 {
    public static String[] myArr, wkArr;
    // input array. It is outside the main method so that it can be accessed in other methods
    public static void Solution() {
        Scanner sc = new Scanner(System.in);
        int num = Integer.parseInt(sc.nextLine() ); // array length
        wkArr = new String[num];                    // workSpace Array
        myArr = new String[num];                    // initialize input arr
        for(int i = 0; i < num; i++)                // fill input arr with input; 10 - 11
            myArr[i] = sc.nextLine();
        // recMergeSort(array, 0, array.length-1); // sort
        mergeSort(0, wkArr.length-1);
        for(String word : myArr)                       // print array using a for each loop
            System.out.println(word);
    }
    public static void mergeSort(int left, int right) {
        int mid = (left + right)/2;
        if(left == right) return;
        mergeSort(left, mid);
        mergeSort(mid+1, right);
        if(right + 1 - left >= 0)
            System.arraycopy(myArr, left, wkArr, left, right + 1 - left);
        int i1 = left, i2 = mid+1;
        for(int curr = left; curr <= right; curr++) {
            if(i1 > mid) {
                myArr[curr] = wkArr[i2++];
            } else if(i2 > right) {
                myArr[curr] = wkArr[i1++];
            } else if(asciiVal(wkArr[i1], wkArr[i2]) ) {
                myArr[curr] = wkArr[i1++];
            } else myArr[curr] = wkArr[i2++];
        }
    }

    // I used the two method merge sort; recMergeSort and merge
    public static void merge(String[] workSpace, int first, int second, int ub) {
        int j = 0;          // workSpace array index
        int lb = first;     // lower bound
        int mid = second-1;
        int n = ub-lb+1;    // number of items

        while(first <= mid && second <= ub) {
            // while the halves are not empty
            if(asciiVal(myArr[first], myArr[second]) ) // asciiVal(first) < asciiVal(second)
                workSpace[j++] = myArr[first++];
            // add myArr[first] to workSpace since it is either lexicographically smaller or has a smaller ascii value
            else workSpace[j++] = myArr[second++];
        }

        while(first <= mid) // checks first half for remaining items
            workSpace[j++] = myArr[first++];
        while(second <= ub) // checks second half for remaining items
            workSpace[j++] = myArr[second++];

        for(j = 0; j < n; j++)
            myArr[lb+j] = workSpace[j]; // copies workSpace into array
    }
    public static void recMergeSort(String[] workSpace, int lb, int ub) {
        if(lb != ub) {
            int mid = (lb + ub)/2;
            recMergeSort(workSpace, lb, mid); // partitions itself until it only has one element left
            recMergeSort(workSpace,mid+1, ub);
            merge(workSpace, lb, mid+1, ub); // merges partitions
        }
    }
    public static boolean asciiVal(String first, String second) {
        int fVal = 0, sVal = 0; // Variables to store the ascii vals
        for(int i = 0; i < first.length(); i++)  // add ascii vals
            fVal += first.charAt(i);
        for(int i = 0; i < second.length(); i++) //  add ascii vals
            sVal += second.charAt(i);

        if(fVal == sVal) {
            /* If the ascii vals are the same,
             * we have to find out if the first or second word comes first in
             * the dictionary; ie, we have to sort lexicographically.
             */ /* <String1>.compareTo(<String2>) = x
                 * x = 0, Strings are lexicographically equal
                 * x < 0, String1 is lexicographically less than String2
                 *       (String1 comes before String2 in the dictionary)
                 * x > 0, String1 is lexicographically more than String2
                 */
            return (first.compareToIgnoreCase(second) > 0);
            /* return (conditional statement), either true or false
             * if true then we add first into the workSpace array and vice versa
             */
        } else return fVal < sVal;
            /* return if fVal is bigger than sVal
             * fVal < sVal is a conditional statement, it is either true or false
             */
    }
}