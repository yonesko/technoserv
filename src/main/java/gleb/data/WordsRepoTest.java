package gleb.data;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class WordsRepoTest implements WordsRepo {
    private Map<Integer, NewsItem> table = new HashMap<>();

    public WordsRepoTest() {
        table.put(1, new NewsItem("На берег в Британии выбросило гигантские трубы", ZonedDateTime.now().minus(Duration.ofDays(1))));
        table.put(2, new NewsItem("В туриндустрии отреагировали на решение России об опасности отдыха в Турции", ZonedDateTime.now().minus(Duration.ofDays(2))));
        table.put(3, new NewsItem("Прохоров продал семь процентов «Русала» по цене ниже рыночной", ZonedDateTime.now().minus(Duration.ofDays(3))));
        table.put(4, new NewsItem("Прохоров продал семь процентов «Русала» по цене ниже рыночной", ZonedDateTime.now().minus(Duration.ofDays(3))));
    }

    @Override
    public Map<String, Integer> allWords() {
        HashMap<String, Integer> ret = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        table.values().stream().map(NewsItem::getContent).forEach(s -> sb.append(s).append('\n'));

        Scanner sc = new Scanner(sb.toString());

        sc.forEachRemaining(word -> ret.compute(word, (s, count) -> count == null ? 1 : count + 1));

        return ret;
    }

    @Override
    public Set<NewsItem> findByWord(String word) {
        return table.values().stream().filter(newsItem -> newsItem.getContent().contains(String.format("%s", word))).collect(Collectors.toSet());
    }
}
