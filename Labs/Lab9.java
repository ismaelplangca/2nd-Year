package Labs;
public class Lab9 {
    public static long q1(long n) {
        return (n == 1) ? 2 : 4*(q1(n--) )-3*n;
    }
    public static float q2(int y, float i, float bal) {
        return (y == 0) ? bal : q2(--y, i, bal*i*0.01f + bal);
    }
    public static float q3(int n) {
        return (n == 1) ? 1 : (366-n)*0.002739726f*q3(--n);
    }
}
