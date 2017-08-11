package gleb;

import gleb.data.WordsRepo;
import gleb.data.WordsRepoTest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("integration.xml")
public class TechnoservApplication {
    public static void main(String[] args) {
        SpringApplication.run(TechnoservApplication.class, args);
    }

    @Bean
    WordsRepo wordsRepoTest() {
        return new WordsRepoTest();
    }

    @Bean
    CommandLineRunner initSolr() {
        return (args) -> {

        };
    }
}
