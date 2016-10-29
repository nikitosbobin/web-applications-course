package core;

import java.util.Scanner;

public class Console {
    private static Scanner scanner;

    public static String readLine() {
        if (scanner == null)
            scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void writeLine(String value) {
        System.out.println(value);
    }

    public static void writeLine(boolean value) {
        writeLine(value + "");
    }

    public static void writeLine(Integer value) {
        writeLine(value + "");
    }

    public static void writeLine(Double value) {
        writeLine(value + "");
    }

    public static void writeLine(Float value) {
        writeLine(value + "");
    }

    public static void writeLine(byte value) {
        writeLine(value + "");
    }

    public static void write(String value) {
        System.out.print(value);
    }

    public static void write(boolean value) {
        write(value + "");
    }

    public static void write(Integer value) {
        write(value + "");
    }

    public static void write(Double value) {
        write(value + "");
    }

    public static void write(Float value) {
        write(value + "");
    }

    public static void write(byte value) {
        write(value + "");
    }
}
