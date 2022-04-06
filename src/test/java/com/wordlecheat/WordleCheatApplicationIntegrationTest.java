package com.wordlecheat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.List;

import com.wordlecheat.builddb.job.BuildDbBatchConfiguration;
import com.wordlecheat.builddb.job.UiJobExecution;
import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.object.LetterEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WordleCheatApplicationIntegrationTest {

    private static final String SINGLE_DICTIONARY_BATCH_JOB_URL = "/batch/" + BuildDbBatchConfiguration.SINGLE_LETTER_STRING_JOB_NAME + "/";
    private static final String DICTIONARY_ENTRY_URL = "/api/dictionaryEntries";
    private static final String ADD_WORDLE_WORDS_BATCH_JOB_URL = "/batch/" + BuildDbBatchConfiguration.ADD_WORDLE_WORDS_JOB_NAME + "/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JobOperator jobOperator;

    /**
     * HTTP POST /batch/buildDbSingleLetterJob/start/{letter}
     * HTTP GET /batch/buildDbSingleLetterJob/{letter}/{startAt}
     * @throws InterruptedException
     * @throws JSONException
     */
    @Test
    @Order(1)
    void testSingleDictionaryLetterJobExecution() throws InterruptedException {
        String postUrl = SINGLE_DICTIONARY_BATCH_JOB_URL + "start/" + LetterEnum.Z.name();
        ResponseEntity<UiJobExecution> jobExecutionResponse = restTemplate.postForEntity(postUrl, null, UiJobExecution.class);
        String getUrl = SINGLE_DICTIONARY_BATCH_JOB_URL + LetterEnum.Z.name() + "/" + jobExecutionResponse.getBody().getJobParameters().get("startAt");
        int timeForJob = 120_000;
        int timeElapsed = 0;
        int timePerCheck = 1_000;
        BatchStatus status = BatchStatus.STARTED;
        while (timeElapsed < timeForJob && status != BatchStatus.COMPLETED) {
            Thread.sleep(timePerCheck);
            jobExecutionResponse = restTemplate.getForEntity(getUrl, UiJobExecution.class);
            status = jobExecutionResponse.getBody().getBatchStatus();
            timeElapsed += timePerCheck;
        }
        assertThat(status).isEqualTo(BatchStatus.COMPLETED);
    }

    /**
     * HTTP GET /api/dictionaryEntries
     */
    @Test
    @Order(2)
    void testWordPlacements() throws JSONException {
        ResponseEntity<String> dictionaryEntriesResponse = restTemplate.getForEntity(DICTIONARY_ENTRY_URL, String.class);
        JSONArray dictionaryEntryJsonArray = new JSONObject(dictionaryEntriesResponse.getBody()).getJSONObject("_embedded").getJSONArray("dictionaryEntries");
        List<DictionaryEntry> dictionaryEntries = new ArrayList<>();
        for (int i = 0; i < dictionaryEntryJsonArray.length(); i++) {
            DictionaryEntry dictionaryEntry = new DictionaryEntry();
            JSONObject json = dictionaryEntryJsonArray.getJSONObject(i);
            dictionaryEntry.setWord(json.getString("word"));
            if (dictionaryEntry.getWord().length() == 5) {
                dictionaryEntry.setFrequency(json.getDouble("frequency"));
                dictionaryEntry.setLetterFrequency(json.getDouble("letterFrequency"));
            }
            dictionaryEntries.add(dictionaryEntry);
        }
        assertThat(dictionaryEntries).extracting(DictionaryEntry::getWord, DictionaryEntry::getFrequency, DictionaryEntry::getLetterFrequency).containsExactly(tuple("Z", null, null),
            tuple("Za", null, null),
            tuple("Zabaism", null, null),
            tuple("Zabism", null, null),
            tuple("Zabian", null, null),
            tuple("Zacco", 0.008982, 20.471100000000003),
            tuple("Zachun", null, null),
            tuple("Zaerthe", null, null),
            tuple("Zaffer", null, null),
            tuple("Zaim", null, null),
            tuple("Zaimet", null, null),
            tuple("Zain", null, null),
            tuple("Zalambdodont", null, null),
            tuple("Zamang", null, null),
            tuple("Zambos", null, null),
            tuple("Zambo", 0.05162, 21.017200000000003),
            tuple("Zamia", 0.039811, 19.326500000000003),
            tuple("Zamindar", null, null),
            tuple("Zamindary", null, null),
            tuple("Zamindari", null, null));
    }

    /**
     * HTTP POST /batch/buildDbAddWordleWordsJob/start
     * HTTP GET /batch/buildDbAddWordleWordsJob/{startAt}
     * HTTP GET /api/dictionaryEntries/search/findByWordIgnoreCase?word=akita
     * @throws InterruptedException
     * @throws JobExecutionNotRunningException
     * @throws NoSuchJobExecutionException
     */
    @Test
    @Order(3)
    void testBuildDbAddWordleWordsJob() throws InterruptedException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        String postUrl = ADD_WORDLE_WORDS_BATCH_JOB_URL + "start";
        ResponseEntity<UiJobExecution> jobExecutionResponse = restTemplate.postForEntity(postUrl, null, UiJobExecution.class);
        String getWordUrl = DICTIONARY_ENTRY_URL + "/search/findByWordIgnoreCase?word=akita";
        int timeForJob = 120_000;
        int timeElapsed = 0;
        int timePerCheck = 1_000;
        HttpStatus status = null;
        while (timeElapsed < timeForJob && status != HttpStatus.OK) {
            Thread.sleep(timePerCheck);
            ResponseEntity<String> dictionaryEntryResponse = restTemplate.getForEntity(getWordUrl, String.class);
            status = dictionaryEntryResponse.getStatusCode();
            timeElapsed += timePerCheck;
        }
        jobOperator.stop(jobExecutionResponse.getBody().getInstanceId());
        String getJobUrl = ADD_WORDLE_WORDS_BATCH_JOB_URL + jobExecutionResponse.getBody().getJobParameters().get("startAt");
        jobExecutionResponse = restTemplate.getForEntity(getJobUrl, UiJobExecution.class);
        assertThat(jobExecutionResponse.getBody().getBatchStatus() == BatchStatus.STOPPED);
    }
}
