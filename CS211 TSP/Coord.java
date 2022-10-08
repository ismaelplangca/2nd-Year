public class Coord {
    private final int id;
    private final double lat;
    private final double lon;

    public Coord(int id, double lat, double lon) {
        this.id  = id;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Coord{"
                + "id=" + id
                + ",lat=" + lat
                + ",lon=" + lon
                + '}';
    }
}
