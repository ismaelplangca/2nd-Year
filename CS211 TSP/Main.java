import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Coord> coordinates;
        try(var coords = Files.lines(Path.of("120schools.txt") ) ) {
            coordinates = coords.map(str -> str.split(",") )
                    .map(arr ->
                            new Coord(
                                    Integer.parseInt(arr[0]),
                                    Double.parseDouble(arr[1]),
                                    Double.parseDouble(arr[2])
                            )
                    ).collect(Collectors.toCollection(ArrayList::new) );
        }
        double[][] matrix = createDistanceMatrix(coordinates);
        double minDistance = Double.MAX_VALUE;
        Solution solution = null;
        for(int i = 0; i < 1000; i++) {
            var sol = solution(matrix);
            if(sol.getDistance() < minDistance)
                solution = sol;
        }
        for(var index : solution.getPath() )
            System.out.println(coordinates.get(index) );
        System.out.println("Total distance:" + solution.getDistance() );
    }
    private static Solution solution(double[][] matrix) {
        ArrayList<Integer> path = nearestNeighbour(matrix);
        path.add(0);
        double totalDistance = totalDistance(path,matrix);
        totalDistance = removeCrossovers(totalDistance,path,matrix);
        return new Solution(path,totalDistance);
    }
    private static double calcDistance(Coord a, Coord b) {
        double radius = 6371;

        double latDiff = a.getLat() - b.getLat();
        double lonDiff = a.getLon() - b.getLon();

        double x = Math.pow(Math.sin(latDiff * 0.5),2)
                + Math.pow(Math.sin(lonDiff * 0.5),2)
                * Math.cos(a.getLat() )
                * Math.cos(b.getLat() );
        return radius * 2 * Math.asin(Math.sqrt(x) );
    }
    private static double[][] createDistanceMatrix(ArrayList<Coord> list) {
        var matrix = new double[121][121];
        for(int i = 0; i < 121; i++)
            for(int j = 0; j < 121; j++)
                matrix[i][j] = calcDistance(list.get(i),list.get(j) );
        return matrix;
    }
    private static ArrayList<Integer> nearestNeighbour(double[][] matrix) {
        boolean[] visited = new boolean[121];
        var path = new ArrayList<Integer>();
        path.add(0);
        visited[0] = true;
        for(int i = 0;;) {
            double localMin = Double.MAX_VALUE;
            int localMinIndex = -1;
            for(int j = 0; j < matrix[i].length; j++) {
                if(j != i && !visited[j] && matrix[i][j] < localMin) {
                    localMin = matrix[i][j];
                    localMinIndex = j;
                }
            }
            if((i = localMinIndex) == -1)
                break;
            path.add(localMinIndex);
            visited[localMinIndex] = true;
        }
        return path;
    }
    private static double totalDistance(ArrayList<Integer> path, double[][] matrix) {
        double distance = 0;
        for(int i = 1; i < path.size(); i++)
            distance += matrix[path.get(i - 1)][path.get(i)];
        return distance;
    }
    private static double removeCrossovers(double totalDistance, ArrayList<Integer> path, double[][] matrix) {
        for(int i = 1; i < path.size() - 1; i++) {
            for(int j = i; j < path.size() - 1; j++) {
                reverse(path.subList(i,j) );
                double distance = totalDistance(path, matrix);
                if(distance >= totalDistance)
                    reverse(path.subList(i,j) );
                else
                    totalDistance = distance;
            }
        }
        return totalDistance;
    }
    private static void reverse(List<Integer> list) {
        for(int i = 0; i < list.size() / 2; i++)
            Collections.swap(list, i, list.size() - i - 1);
    }
}
