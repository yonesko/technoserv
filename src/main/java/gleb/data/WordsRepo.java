package gleb.data;

import java.util.Map;
import java.util.Set;

public interface WordsRepo {
    Map<String, Integer> allWords();

    Set<NewsItem> findByWord(String word);
}
