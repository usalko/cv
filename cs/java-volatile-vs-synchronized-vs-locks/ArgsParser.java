import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class ArgsParser {

    final List<String> argsList = new ArrayList<String>();
    final List<String> optsList = new ArrayList<String>();
    final List<String> doubleOptsList = new ArrayList<String>();

    final Map<String, String> options = new HashMap<>();
    final Map<String, String> comments = new HashMap<>();

    ArgsParser(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].charAt(1) == '-') {
                        int len = 0;
                        String argString = args[i].toString();
                        len = argString.length();
                        this.doubleOptsList.add(argString.substring(2, len));
                    } else {
                        this.optsList.add(args[i]);
                    }
                    break;
                default:
                    System.out.println("Add a default arg.");
                    this.argsList.add(args[i]);
                    break;
            }
        }
        // Parse list
        options.putAll(optsList.stream().collect(Collectors.toMap(option -> option.substring(1), option -> "true")));
        // Parse double list
        options.putAll(doubleOptsList.stream().collect(Collectors.toMap(option -> option.split("=")[0], option -> option.split("=")[1])));
    }

    public boolean getBoolOption(String comment, String key, boolean defaultValue) {
        comments.put(key, comment);
        return Boolean.parseBoolean(options.getOrDefault(key, Boolean.toString(defaultValue)));
    }

    public int getIntOption(String comment, String key, int defaultValue) {
        comments.put(key, comment);
        return Integer.parseInt(options.getOrDefault(key, Integer.toString(defaultValue)));
    }

    public double getDoubleOption(String comment, String key, double defaultValue) {
        comments.put(key, comment);
        return Double.parseDouble(options.getOrDefault(key, Double.toString(defaultValue)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getTypeOption(String comment, String key, Function<String, Class<? extends T>> valueMapper)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        comments.put(key, comment);
        Class<? extends T> classValue = valueMapper.apply(options.getOrDefault(key, "volatile-value"));
        return (T) classValue.getDeclaredConstructors()[0].newInstance();
    }

    public void print() {
        options.forEach((key, value) -> {
            System.out.printf("%s is %s\n", key, value);
        });
    }

    public void help() {
        options.forEach((key, value) -> {
            String comment = comments.get(key);
            System.out.printf("Option %s // %s // value is %s\n", key, comment, value);
        });
    }

}