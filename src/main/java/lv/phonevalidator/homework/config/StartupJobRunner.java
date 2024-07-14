package lv.phonevalidator.homework.config;

import lv.phonevalidator.homework.exception.PhoneValidatorException;
import lv.phonevalidator.homework.service.WikipediaReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupJobRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartupJobRunner.class);

    private final WikipediaReader wikiReader;

    public StartupJobRunner(WikipediaReader wikiReader) {
        this.wikiReader = wikiReader;
    }

    @Bean
    public CommandLineRunner autorun() {
        return args -> {
            try {
                wikiReader.populateDatabase();
            } catch (PhoneValidatorException ex) {
                LOGGER.error("Could not populate database on application start", ex);
            }
        };
    }
}