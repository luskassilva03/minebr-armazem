package net.minebr.armazem.settings;

import lombok.Getter;

import java.util.Map;

@Getter
public class PlantationConfig {
    // Getters e setters
    private Map<String, PriceConfig> prices;

    public void setPrices(Map<String, PriceConfig> prices) {
        this.prices = prices;
    }
}
