import java.util.ArrayList;

public class Solution {
    private final ArrayList<Integer> path;
    private final double distance;
    public Solution(ArrayList<Integer> path, double distance) {
        this.path = path;
        this.distance = distance;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }
    public double getDistance() {
        return distance;
    }
}
