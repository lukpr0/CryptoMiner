package main.java;

public class PerformanceTest {
    public static void main(String[] args) {
        String seed;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            seed=Long.toHexString(Double.doubleToLongBits(Math.random()));
        }
        System.out.printf("Random : %d%n", System.currentTimeMillis()-start);
        start = System.currentTimeMillis();
        long j = 0L;
        for (int i = 0; i < 100000000; i++) {
            seed=Long.toHexString(Double.doubleToLongBits(j));
            j++;
        }
        System.out.printf("Random : %d%n", System.currentTimeMillis()-start);
    }
}
