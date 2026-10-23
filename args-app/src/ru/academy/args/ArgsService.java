package ru.academy.args;

public class ArgsService {

    public String describe(String[] args) {
        if (args == null || args.length == 0) {
            return "Аргументы не переданы";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Передано аргументов: ").append(args.length);

        long sum = 0;
        boolean hasNumbers = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            sb.append("\n[").append(i).append("] ").append(arg);

            if (isNumber(arg)) {
                sb.append(" (число)");
                sum += Integer.parseInt(arg);
                hasNumbers = true;
            }
        }

        if (hasNumbers) {
            sb.append("\nСумма чисел: ").append(sum);
        }

        return sb.toString();
    }

    private boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
