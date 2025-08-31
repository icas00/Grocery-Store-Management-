package grocery.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to integrate with Google Maps API for store location.
 */
@Service
@RequiredArgsConstructor
public class StoreLocationService {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Get latitude and longitude for given address using Google Maps Geocoding API.
     */
    public Map<String, Double> getLatLongFromAddress(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                address.replace(" ", "+") +
                "&key=" + googleMapsApiKey;

        String response = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode location = root.path("results").get(0).path("geometry").path("location");
            double lat = location.path("lat").asDouble();
            double lng = location.path("lng").asDouble();

            Map<String, Double> latLngMap = new HashMap<>();
            latLngMap.put("latitude", lat);
            latLngMap.put("longitude", lng);
            return latLngMap;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get location from Google Maps API", e);
        }
    }
}