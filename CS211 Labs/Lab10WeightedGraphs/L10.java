package Lab10WeightedGraphs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class L10 {
    public static void solution() {
        Scanner file = null;
        try(var scanIn = new Scanner(new File("schools39.txt") ) ) {
            file = scanIn;
        } catch(IOException ignored) {}

        double[] lats = new double[39];
        double[] lons = new double[39];
        for(int index = 0; file.hasNext(); index++) {
            String line = file.nextLine();
            int splitPos = line.indexOf(' ');
            lats[index] = Double.parseDouble(line.substring(0,splitPos++) );
            lons[index] = Double.parseDouble(line.substring(splitPos) );
        }

        for(int i = 0; i < lats.length; i++)
            System.out.println(lats[i] + "," + lons[i]);

        System.out.println();
        double[] tenthDistances = calcDists(lats, lons);
        for(double d : tenthDistances)
            System.out.println(d);

        System.out.println();
        bubbleSort(lats,lons,tenthDistances);
        System.out.println(lats[0] + "," + lons[0] + ": " + tenthDistances[0]);
    }

    private static double distBetwTwoPoints(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371d;
        double toRadian = Math.PI/180;
        double phi1 = lat1 * toRadian;
        double phi2 = lat2 * toRadian;
        double deltPhi = (lat2 - lat1) * toRadian;
        double deltLambda = (lon2 - lon1) * toRadian;

        double a =
                Math.sin(deltPhi*0.5) * Math.sin(deltPhi*0.5) +
                        Math.cos(phi1) * Math.cos(phi2) *
                                Math.sin(deltLambda*0.5) * Math.sin(deltLambda*0.5);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a) );
        return earthRadius * c;
    }

    public static void swap(double[] arr, int index) {
        double temp = arr[index];
        arr[index] = arr[index+1];
        arr[index+1] = temp;
    }

    public static void bubbleSort(double[] lats, double[] lons, double[] tenthDists) {
        int arrLen = tenthDists.length;
        for(int i = 0; i < arrLen-1; i++)
            for(int j = 0; j < arrLen-i-1; j++)
                if(tenthDists[j] > tenthDists[j+1]) {
                    swap(tenthDists, j);
                    swap(lats, j);
                    swap(lons, j);
                }
    }

    public static double[] calcDists(double[] lats, double[] lons) {
        int arrLen = lats.length;
        double[] allTenthDistsFromI = new double[arrLen];

        for(int i = 0; i < arrLen; i++) {
            double[] allDistsFromI = new double[arrLen];
            for(int j = 0, index = 0; j < arrLen; j++, index++)
                allDistsFromI[index] = distBetwTwoPoints(lats[i],lons[i],lats[j],lons[j]);
            Arrays.sort(allDistsFromI);
            allTenthDistsFromI[i] = allDistsFromI[10];
        }

        return allTenthDistsFromI;
    }
}
