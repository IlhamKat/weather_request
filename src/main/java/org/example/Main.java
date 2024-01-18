package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        List<GermanCity> germanyCities = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/main/resources/Germany_Cities.json");
            String absolutePath = file.getAbsolutePath();
            germanyCities = objectMapper.readValue(new File(absolutePath), new TypeReference<List<GermanCity>>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Geben Sie den Namen der gesuchten Stadt ein: ");
        String inputCity = scanner.next();
        inputCity = inputCity.substring(0, 1).toUpperCase() + inputCity.substring(1).toLowerCase();

        //String inputCity = "Berlin";

        GermanCity selectedCity = null;

        try {
            for (GermanCity city : germanyCities) {
                if (city.getCity().equals(inputCity)) {
                    selectedCity = city;
                    break;
                }
            }
                if (selectedCity == null) {
                    throw new CityNotFoundException("Stadt wurde nicht gefunden.");
                }
        } catch (CityNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (selectedCity != null){
            HttpRequest gettCity = HttpRequest.newBuilder()
                    .uri(new URI("https://api.brightsky.dev/current_weather?lat=" + selectedCity.getLat() + "&lon=" + selectedCity.getLng()))
                    .header("Accept", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(gettCity, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = response.body();


            try {
                ObjectMapper objectMapper = new ObjectMapper();
                WeatherData weatherData = objectMapper.readValue(jsonResponse, WeatherData.class);

                Weather weather = weatherData.getWeather();
                System.out.println("Stadt: " + inputCity);
                System.out.println("Datum/Uhrzeit: " + weather.getTimestamp());
                System.out.println("Temperatur: " + weather.getTemperature() + " Grad Celsius");
                System.out.println("Wetterlage: " + weather.getCondition());
                System.out.println("Icon: " + weather.getIcon());
                System.out.println("Bewölkung: " + weather.getCloud_cover());

                List<Source> sources = weatherData.getSources();
                for (Source source : sources) {
                    System.out.println("Station: " + source.getStation_name());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
