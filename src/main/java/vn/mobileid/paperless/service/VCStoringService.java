package vn.mobileid.paperless.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VCStoringService {
    Map<String, String> map = new HashMap<>();

    //add JsonNode to storage
    public void store(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }

    public void remove(String key) {
        map.remove(key);
    }
}
