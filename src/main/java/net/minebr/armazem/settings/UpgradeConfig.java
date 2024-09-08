package net.minebr.armazem.settings;

import lombok.Getter;

@Getter
public class UpgradeConfig {

    double addLevel;
    double priceCoins;
    double multiplyCoins;
    double priceTokens;
    double multiplyTokens;

    public UpgradeConfig(double addLevel, double priceCoins, double multiplyCoins, double priceTokens, double multiplyTokens) {
        this.addLevel = addLevel;
        this.priceCoins = priceCoins;
        this.multiplyCoins = multiplyCoins;
        this.priceTokens = priceTokens;
        this.multiplyTokens = multiplyTokens;
    }
}
