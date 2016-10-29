package core;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Individual_3 implements Result {
    private String[] input;

    public Individual_3(String input) {
        this.input = input == null ? null : input.split("[, _]");
    }

    public Individual_3(Double[] input) { }

    @Override
    public String getResult() {
        if (input == null) {
            return "0";
        }
        HashMap<String, Integer> repetitions = new HashMap<>();
        for (String word : input) {
            if (!repetitions.containsKey(word)) {
                repetitions.put(word, 1);
            }else {
                int currentValue = repetitions.get(word);
                repetitions.replace(word, currentValue + 1);
            }
        }
        int count = 0;
        for (Map.Entry<String, Integer> entry : repetitions.entrySet().stream().filter(x -> x.getValue() > 1).collect(Collectors.toList())) {
            count += entry.getValue();
        }
        return count + "";
    }
}
