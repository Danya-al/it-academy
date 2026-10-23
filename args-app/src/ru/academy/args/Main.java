package ru.academy.args;

public class Main {
    public static void main(String[] args) {
        ArgsService service = new ArgsService();
        String result = service.describe(args);
        System.out.println(result);
    }
}
