package gleb.web;

import gleb.data.NewsItem;
import gleb.data.WordsRepo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZonedDateTime;
import java.util.*;

@org.springframework.stereotype.Controller
public class Controller {
    private WordsRepo wordsRepo;

    public Controller(WordsRepo wordsRepo) {
        this.wordsRepo = wordsRepo;
    }

    @RequestMapping
    public String allWords(Model model) {
        Map<String, Integer> ret = new HashMap<>();

        ret = wordsRepo.allWords();

        model.addAttribute("words", ret);

        return "index";
    }

    @GetMapping("/refine")
    public String refineSearch(@RequestParam("word") String word, Model model) {
        Set<NewsItem> newsItems = wordsRepo.findByWord(word);

        List<String> cumulativeContent = new ArrayList<>(newsItems.size());
        Map<ZonedDateTime, Integer> chart = new HashMap<>();
        newsItems.forEach(newsItem -> {
            cumulativeContent.add(newsItem.getContent());
            chart.compute(newsItem.getPublicationDateTime(), (instant, count) -> count == null ? 1 : count + 1);
        });

        model.addAttribute("cumulativeContent", cumulativeContent);
        model.addAttribute("chart", chart);

        return "refine";
    }
}
