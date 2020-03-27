package io.netty.example.mystudy.executor;

public class Test {
    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(Integer.toBinaryString(1));
        System.out.println(Integer.toBinaryString((1 << 29)));
        System.out.println(Integer.toBinaryString((1 << 29) - 1));


        System.out.println(Integer.toBinaryString((1 << 29) - 1));

        System.out.println("======状态=========");
        System.out.println(Integer.toBinaryString(-1 << 29) + "," + (-1 << 29));
        System.out.println(Integer.toBinaryString(0 << 29) + "," + (0 << 29));
        System.out.println(Integer.toBinaryString(1 << 29) + "," + (1 << 29));
        System.out.println(Integer.toBinaryString(2 << 29) + "," + (2 << 29));
        System.out.println(Integer.toBinaryString(3 << 29) + "," + (3 << 29));
    }
}
