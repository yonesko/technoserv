package gleb.web;

import gleb.data.WordDetailDTO;
import gleb.data.WordsRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    public WordDetailDTO refineSearch(@RequestParam("word") String word) {
        return wordsRepo.findByWord(word);
    }
}
