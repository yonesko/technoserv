package gleb.web;

import gleb.data.NewsItem;
import gleb.data.WordsRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.*;

@RestController
public class Controller {
    private WordsRepo wordsRepo;

    public Controller(WordsRepo wordsRepo) {
        this.wordsRepo = wordsRepo;
    }

    @GetMapping
    public Map<String, Integer> allWords() {
        Map<String, Integer> ret = new HashMap<>();

        ret = wordsRepo.allWords();

        return ret;
    }

    @GetMapping("/refine")
    public Map<String, Object> refineSearch(@RequestParam("word") String word) {
        Set<NewsItem> newsItems = wordsRepo.findByWord(word);

        List<String> cumulativeContent = new ArrayList<>(newsItems.size());
        Map<ZonedDateTime, Integer> chart = new HashMap<>();
        newsItems.forEach(newsItem -> {
            cumulativeContent.add(newsItem.getContent());
            chart.compute(newsItem.getPublicationDateTime(), (instant, count) -> count == null ? 1 : count + 1);
        });

        Map<String, Object> ret = new HashMap<>();

        ret.put("cumulativeContent", cumulativeContent);
        ret.put("chart", chart);

        return ret;
    }
}
