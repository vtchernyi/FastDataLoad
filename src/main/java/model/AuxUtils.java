package model;

public class AuxUtils {
    public static void printElapsedTime(String msg, long t) {
        System.out.println(msg + ", seconds elapsed: " + (System.currentTimeMillis() - t) / 1000.0);
    }
}
