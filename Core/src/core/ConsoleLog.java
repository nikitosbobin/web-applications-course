package core;

public class ConsoleLog implements ILog {
    private String prefix;

    public ConsoleLog() {
        prefix = "";
    }

    @Override
    public void info(String message) {
        Console.write(String.format("%s INFO %s\n", prefix, message));
    }

    @Override
    public void debug(String message) {
        Console.write(String.format("%s DEBUG %s\n", prefix, message));
    }

    @Override
    public void error(String message) {
        Console.write(String.format("%s ERROR %s\n", prefix, message));
    }

    public ConsoleLog withPrefix(String prefix) {
        ConsoleLog result = new ConsoleLog();
        result.prefix = "[" + prefix + "] ";
        return result;
    }
}
