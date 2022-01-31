package com.wordlecheat.builddb;

import java.util.Date;
import java.util.List;

import com.wordlecheat.dictionary.DictionaryFileReader;
import com.wordlecheat.dictionary.object.Definition;
import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.object.LetterEnum;
import com.wordlecheat.dictionary.object.WordleWords;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.dictionary.service.WordApiService;

import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.wordlecheat.dictionary.repository")
@EntityScan("com.wordlecheat.dictionary.object")
@ComponentScan("com.wordlecheat.dictionary.service")
public class BuildDbApplication {
    
    private static final Logger log = LoggerFactory.getLogger(BuildDbApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BuildDbApplication.class, args);
	}

	@Bean
	public CommandLineRunner build(DictionaryEntryRepository dictionaryEntryRepository, WordApiService wordApiService) {
		return (args) -> {
			log.info("Beginning database build");
			DictionaryFileReader dictionaryFileReader = new DictionaryFileReader();
			String entry;
			while ((entry = dictionaryFileReader.getNextDictionaryEntry()) != null) {
				DictionaryEntry dictionaryEntry = null;
				try {
					dictionaryEntry = new DictionaryEntry(entry);
				} catch (StringIndexOutOfBoundsException ex) {
					log.warn("Incorrectly formatted dictionary entry: {}", entry);
					continue;
				}
				List<DictionaryEntry> entries = dictionaryEntryRepository.findByWordIgnoreCase(dictionaryEntry.getWord());
				if (entries != null && !entries.isEmpty()) {
					dictionaryEntry = entries.get(0);
					dictionaryEntry.addDefinition(new Definition(entry));
				}
				if (dictionaryEntry.isValidWordlerWord()) {
                    if (dictionaryEntry.getFrequency() == null && dictionaryEntry.getLastCheckedFrequency() == null) {
                        dictionaryEntry.setLastCheckedFrequency(new Date());
                        try {
                            dictionaryEntry.setFrequency(wordApiService.getWordFrequency(dictionaryEntry.getWord()));
                        } catch (Exception ex) {
                            String message = String.format("Failed to get frequency for word: %s", dictionaryEntry.getWord());
                            log.error(message, ex);
                        }
                    }
                    dictionaryEntry.setLetterFrequency(0.0);
                    for (LetterEnum letter : LetterEnum.values()) {
                        if (dictionaryEntry.getWord().toLowerCase().contains(letter.toString().toLowerCase())) {
                            dictionaryEntry.setLetterFrequency(dictionaryEntry.getLetterFrequency() + letter.getFrequency());
                        }
                    }
				}
				dictionaryEntryRepository.save(dictionaryEntry);
			}
			addWordleWords(dictionaryEntryRepository, wordApiService);
			log.info("Finished database build");
		};
	}

	private void addWordleWords(DictionaryEntryRepository dictionaryEntryRepository, WordApiService wordApiService) {
        for (String wordleWord : WordleWords.WORDLE_WORDS) {
            List<DictionaryEntry> dictionaryEntries = dictionaryEntryRepository.findByWordIgnoreCase(wordleWord);
            if (dictionaryEntries.isEmpty()) {
                DictionaryEntry dictionaryEntry = new DictionaryEntry();
                log.info("Adding {}", WordUtils.capitalize(wordleWord));
                dictionaryEntry.setWord(WordUtils.capitalize(wordleWord));
                dictionaryEntry.setLastCheckedFrequency(new Date());
                try {
                    dictionaryEntry.setFrequency(wordApiService.getWordFrequency(dictionaryEntry.getWord()));
                } catch (Exception ex) {
                    String message = String.format("Failed to get frequency for word: %s", dictionaryEntry.getWord());
                    log.error(message, ex);
                }
                dictionaryEntry.setLetterFrequency(0.0);
                for (LetterEnum letter : LetterEnum.values()) {
                    if (dictionaryEntry.getWord().toLowerCase().contains(letter.toString().toLowerCase())) {
                        dictionaryEntry.setLetterFrequency(dictionaryEntry.getLetterFrequency() + letter.getFrequency());
                    }
                }
                dictionaryEntryRepository.save(dictionaryEntry);
            }
        }
    }
}
