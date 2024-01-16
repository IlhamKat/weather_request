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

        List<GermanyCity> germanyCities = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            germanyCities = objectMapper.readValue(new File("C:\\Users\\katir\\Desktop\\WetterAPI\\Germany_Cities.json"), new TypeReference<List<GermanyCity>>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Geben Sie den Namen der gesuchten Stadt ein: ");
        String gesuchteStadt = scanner.next();
        //String gesuchteStadt = "Berlin";

        GermanyCity ausgewaehlteStadt = null;

        try {
            for (GermanyCity city : germanyCities) {
                if (city.getCity().equals(gesuchteStadt)) {
                    ausgewaehlteStadt = city;
                    break;
                }
            }
                if (ausgewaehlteStadt == null) {
                    throw new StadtNichtGefundenException("Stadt wurde nicht gefunden.");
                }
        } catch (StadtNichtGefundenException e) {
            System.out.println(e.getMessage());
        }

        if (ausgewaehlteStadt != null){
            HttpRequest gettCity = HttpRequest.newBuilder()
                    .uri(new URI("https://api.brightsky.dev/current_weather?lat=" + ausgewaehlteStadt.getLat() + "8&lon=" + ausgewaehlteStadt.getLng()))
                    .header("Accept", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(gettCity, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = response.body();


            try {
                ObjectMapper objectMapper = new ObjectMapper();
                WeatherData weatherData = objectMapper.readValue(jsonResponse, WeatherData.class);

                Weather weather = weatherData.getWeather();
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
