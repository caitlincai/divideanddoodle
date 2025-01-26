import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WordGenerator {
    public static final int numberOfWords = 16;
    public static ArrayList<String> words = new ArrayList<>(Arrays.asList("hammerhead shark", "rocking chair", "horse", "wheelbarrow", "notebook", "waterfall", "squirrel", "frog", "house", "car", "owl", "computer", "cup", "floor", "solar system", "sky"));

    public static String getWord() {
        return generate(numberOfWords);
    }

    public static String generate(int n) {
        Random random = new Random();
        int number = random.nextInt(numberOfWords);
        return words.get(number);
    }
}
