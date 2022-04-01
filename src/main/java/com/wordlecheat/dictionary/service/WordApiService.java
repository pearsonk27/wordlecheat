package com.wordlecheat.dictionary.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordlecheat.dictionary.object.DataMuseWord;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordApiService {

    private static final Logger log = LoggerFactory.getLogger(WordApiService.class);

    private static final int API_CALL_COUNT_LIMIT = 100_000;
	
	private HttpURLConnection conn;

    private static long apiCallCount;

    @Autowired
    public WordApiService(DictionaryEntryRepository dictionaryEntryRepository) {
        apiCallCount = dictionaryEntryRepository.countByLastCheckedFrequencyBetween(getStartOfToday(), getEndOfToday());
        log.info("Setting apiCallCount to {}", apiCallCount);
    }
    
    public Double getWordFrequency(String word) {
        Double frequency = null;

        if (apiCallCount >= API_CALL_COUNT_LIMIT) {
            return frequency;
        }

        BufferedReader reader;
		String line;
		StringBuilder responseContent = new StringBuilder();
		try{
			URL url = new URL(String.format("https://api.datamuse.com/words?sp=%s&md=f&max=1", word));
			conn = (HttpURLConnection) url.openConnection();
            apiCallCount++;
            if (apiCallCount >= API_CALL_COUNT_LIMIT) {
                log.warn("Datamuse daily call count limit reached, no more calls will be made");
            }
			
			// Request setup
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);// 5000 milliseconds = 5 seconds
			conn.setReadTimeout(5000);
			
			// Test if the response from the server is successful
			int status = conn.getResponseCode();
			
			if (status >= 300) {
				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			}
			else {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();

                ObjectMapper objectMapper = new ObjectMapper();
                DataMuseWord dataMuseWord = objectMapper.readValue(responseContent.toString(), DataMuseWord[].class)[0];
                String frequencyString = dataMuseWord.getTags().get(0);
                frequency = Double.parseDouble(frequencyString.substring(2, frequencyString.length()));
			}
			log.debug("response code: {}", status);
			log.debug("response: {}", responseContent);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			conn.disconnect();
		}

        return frequency;
    }

    private static Date getStartOfToday() {
        LocalDateTime localDateTime = dateToLocalDateTime(new Date());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }
    
    private static Date getEndOfToday() {
        LocalDateTime localDateTime = dateToLocalDateTime(new Date());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }
    
    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    
    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
