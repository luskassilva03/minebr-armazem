package net.minebr.armazem.functions;

import net.minebr.armazem.ArmazemMain;
import net.minebr.armazem.settings.PlantationConfig;
import net.minebr.armazem.settings.PriceConfig;

public class PriceFetcher {

    private final ArmazemMain main = ArmazemMain.getPlugin();

    /**
     * Obtém o valor de um provedor específico para uma plantação.
     *
     * @param plantationKey A chave da plantação (por exemplo, "CACTUS").
     * @param provider O provedor que você deseja consultar (por exemplo, "cash").
     * @return O valor associado ao provedor, ou 0 se não encontrado.
     */
    public double getPriceForProvider(String plantationKey, String provider) {
        PlantationConfig plantationConfig = main.getPlantationsType().get(plantationKey);

        if (plantationConfig == null) {
            return 0; // Ou lançar uma exceção se preferir
        }

        return plantationConfig.getPrices().values().stream()
                .filter(priceConfig -> priceConfig.getProvider().equals(provider))
                .mapToDouble(PriceConfig::getPrice)
                .findFirst()
                .orElse(0); // Valor padrão se o provedor não for encontrado
    }
}
