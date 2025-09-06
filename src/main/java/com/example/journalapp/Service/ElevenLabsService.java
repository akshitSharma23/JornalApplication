package com.example.journalapp.Service;

import com.example.journalapp.Cache.AppCache;
import com.example.journalapp.Entity.JournalEntry;
import com.example.journalapp.Entity.WeatherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ElevenLabsService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${ElevenLabs.API.Key}")
    private String apiKey;
//    @Value("${ElevenLabs.API.Endpoint}")
//    private String API;

    @Autowired
    AppCache appCache;

    public ResponseEntity<?> getAudio(JournalEntry journalEntry){
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.set("xi-api-key",apiKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String body = "{\n" +
                "  \"text\": \"" + journalEntry.getTitle() + " " + journalEntry.getContent() + "\",\n" +
                "  \"model_id\": \"eleven_multilingual_v2\"\n" +
                "}";
        HttpEntity<String> httpEntity=new HttpEntity<>(body,httpHeaders);
        ResponseEntity<byte[]> bytes;
        try{
            bytes=restTemplate.exchange(appCache.getAppCache().get("ELEVENLABS_API"),HttpMethod.POST,httpEntity,byte[].class);
//            bytes=restTemplate.exchange(API,HttpMethod.POST,httpEntity,byte[].class);
        }catch (Exception e){
           return new ResponseEntity<>(e,HttpStatus.NOT_FOUND);
        }
        saveToFile(bytes.getBody(),"audio/output.mp3");
        return bytes;
    }

    private void saveToFile(byte[] data, String filePath) {
        try {
            Path path= Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.write(path,data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
