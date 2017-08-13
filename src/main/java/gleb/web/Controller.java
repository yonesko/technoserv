package gleb.web;

import gleb.data.NewsItem;
import gleb.data.WordsRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("/wordStat")
public class Controller {
    private WordsRepo wordsRepo;

    public Controller(WordsRepo wordsRepo) {
        this.wordsRepo = wordsRepo;
    }

    @GetMapping
    public Map<String, Integer> allWords() {
        return wordsRepo.wordStat();
    }

    @GetMapping("/detail")
    public Map<String, Object> refineSearch(@RequestParam("word") String word) {
        Set<NewsItem> newsItems = wordsRepo.findByWord(word);

        List<String> contentList = new ArrayList<>(newsItems.size());
        Map<String, Integer> chart = new HashMap<>();
        newsItems.forEach(newsItem -> {
            contentList.add(newsItem.getContent());
            chart.compute(newsItem.getPublicationDateTime().toLocalDate().toString(), (instant, count) -> count == null ? 1 : count + 1);
        });

        Map<String, Object> model = new HashMap<>();
        model.put("contentList", contentList);
        model.put("chart", chart);

        return model;
    }
}
