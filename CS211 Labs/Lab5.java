package Lab5Quicksort;

import java.util.Arrays;
import java.util.Comparator;
import static MyUtil.Printer.*;

public class Lab5 {
    public static void solution() {
        int start = 5;
        int end   = 50;
        collatzNumber[] arr2Col = new collatzNumber[end-start+1];
        for(int i = start, index = 0; i <= end; i++, index++)
            arr2Col[index] = new collatzNumber(i);

        kwik.sort(arr2Col);

        printArr(arr2Col);
    }

    public static collatzNumber lab5Java18Solution(int[] arr, int n) {
        return Arrays.stream(arr)
                .boxed()
                .map(collatzNumber::new)
                .sorted(Comparator.comparing(collatzNumber::getCollatzLength) )
                .skip(n-1)
                .findFirst()
                .orElseThrow();
    }
}

class Quick {

    private Quick() {}

    public static void sort(int[] arr) {
        sort(arr, 0, arr.length-1);
        insertionSort(arr, 0, arr.length);
    }

    public static void sort(int[] arr, int left, int right) {
        if(right - left + 1 > 3) {
            long pivot = medianOf3(arr, left, right);
            int partition = partition(arr, left, right, pivot);

            sort(arr, left, partition-1);
            // System.out.println("Left sorted");

            sort(arr, partition+1, right);
            // System.out.println("Right sorted");
        }
    }

    private static int partition(int[] arr, int left, int right, long pivot) {
        int leftPtr = left-1;
        int rightPtr = right;
        while(true) {
            while(arr[++leftPtr] < pivot) {}
            while(arr[--rightPtr] > pivot) {}

            if(leftPtr >= rightPtr)
                break;
            else
                swap(arr, leftPtr, rightPtr);
        }
        swap(arr, leftPtr, right);
        return leftPtr;
    }

    private static long medianOf3(int[] arr, int left, int right) {
        int center = (left+right)/2;

        if(arr[left] > arr[center])
            swap(arr, left, center);

        if(arr[left] > arr[right])
            swap(arr, left, right);

        if(arr[center] > arr[right])
            swap(arr, center, right);

        swap(arr, center, right-1);
        return arr[right-1];
    }

    private static void swap(int[] arr, int i1, int i2) {
        int temp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = temp;
    }

    private static void insertionSort(int[] arr, int left, int right) {
        for(int i, k = left; ++k < right; ) {
            int arri = arr[i = k];
            if(arri < arr[i - 1]) {
                while(--i >= left && arri < arr[i])
                    arr[i + 1] = arr[i];
                arr[i + 1] = arri;
            }
        }
    }
}

class kwik {

    private kwik() {}

    public static void sort(collatzNumber[] arr) {
        sort(arr, 0, arr.length-1);
    }

    public static void sort(collatzNumber[] arr, int left, int right) {
        if(right - left + 1 < 27) {
            insertionSort(arr, left, right);
        } else {
            long pivot = medianOf3(arr, left, right);

            int partition = partition(arr, left, right, pivot);

            sort(arr, left, partition-1);
            sort(arr, partition+1, right);
        }
    }

    private static int partition(collatzNumber[] arr, int left, int right, long pivot) {
        int leftPtr = left-1;
        int rightPtr = right;
        while(true) {
            while(arr[++leftPtr].getCollatzLength() < pivot);
            while(arr[--rightPtr].getCollatzLength() > pivot);

            if(leftPtr >= rightPtr)
                break;
            else
                swap(arr, leftPtr, rightPtr);
        }
        swap(arr, leftPtr, right);
        return leftPtr;
    }

    private static long medianOf3(collatzNumber[] arr, int left, int right) {
        int center = (left+right)/2;

        if(arr[left].getCollatzLength() > arr[center].getCollatzLength() )
            swap(arr, left, center);

        if(arr[left].getCollatzLength() > arr[right].getCollatzLength() )
            swap(arr, left, right);

        if(arr[center].getCollatzLength() > arr[right].getCollatzLength() )
            swap(arr, center, right);

        swap(arr, center, right-1);
        return arr[right-1].getCollatzLength();
    }

    private static void swap(collatzNumber[] arr, int i1, int i2) {
        collatzNumber temp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = temp;
    }

    private static void insertionSort(collatzNumber[] arr, int left, int right) {
        for(int i, k = left; ++k <= right;) {
            collatzNumber arri = arr[i = k];
            if(arri.getCollatzLength() < arr[i - 1].getCollatzLength()) {
                while(--i >= left && arri.getCollatzLength() < arr[i].getCollatzLength() )
                    arr[i + 1] = arr[i];
                arr[i + 1] = arri;
            }
        }
    }
}

class collatzNumber {
    private final int num;
    private final int collatzLength;

    public collatzNumber(int num) {
        this.num = num;
        int steps = 0;
        while(num > 1) {
            num = num % 2 == 0
                ? num >> 2
                : num * 3 + 1;
            steps++;
        }
        collatzLength = steps;
    }

    public int getNum() {
        return num;
    }

    public int getCollatzLength() {
        return collatzLength;
    }

    @Override
    public String toString() {
        return num+"," +collatzLength;
    }
}