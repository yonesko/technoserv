package gleb.data;

import java.util.Map;

public interface WordsRepo {
    Map<String, Long> wordStat();

    WordDetail findByWord(String word);
}
