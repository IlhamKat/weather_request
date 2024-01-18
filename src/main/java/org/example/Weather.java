package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    private String timestamp;
    private int cloud_cover;
    private String condition;
    private double temperature;
    private Map<String, Integer> fallbackSourceIds;
    private String icon;

    public Map<String, Integer> getFallbackSourceIds() {
        return fallbackSourceIds;
    }

    public void setFallbackSourceIds(Map<String, Integer> fallbackSourceIds) {
        this.fallbackSourceIds = fallbackSourceIds;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getCloud_cover() {
        return cloud_cover;
    }

    public void setCloud_cover(int cloud_cover) {
        this.cloud_cover = cloud_cover;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }


}
