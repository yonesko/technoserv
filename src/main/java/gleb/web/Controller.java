package gleb.web;

import gleb.data.WordDetail;
import gleb.data.WordsRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/wordStat")
public class Controller {
    private final WordsRepo wordsRepo;

    public Controller(WordsRepo wordsRepo) {
        this.wordsRepo = wordsRepo;
    }

    @GetMapping
    public Map<String, Long> allWords() {
        return wordsRepo.wordStat();
    }

    @GetMapping("/detail")
    public WordDetail refineSearch(@RequestParam("word") String word) {
        return wordsRepo.findByWord(word);
    }
}
